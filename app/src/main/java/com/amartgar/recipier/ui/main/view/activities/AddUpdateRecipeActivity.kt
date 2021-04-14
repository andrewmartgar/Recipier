package com.amartgar.recipier.ui.main.view.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.amartgar.recipier.R
import com.amartgar.recipier.application.RecipierApplication
import com.amartgar.recipier.data.model.entities.Recipier
import com.amartgar.recipier.databinding.ActivityAddUpdateRecipeBinding
import com.amartgar.recipier.databinding.DialogCustomImageSelectionBinding
import com.amartgar.recipier.databinding.DialogCustomListBinding
import com.amartgar.recipier.ui.main.adapter.ItemListAdapter
import com.amartgar.recipier.utils.Constants
import com.amartgar.recipier.viewmodel.RecipierViewModel
import com.amartgar.recipier.viewmodel.RecipierViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import es.dmoral.toasty.Toasty
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AddUpdateRecipeActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val CAMERA = 1
        private const val GALLERY = 2
        private const val IMAGE_DIRECTORY = "RecipierImages"
    }

    private lateinit var mBinding: ActivityAddUpdateRecipeBinding
    private lateinit var mCustomDialog: Dialog
    private var mImagePath: String = ""

    private val mRecipierViewModel: RecipierViewModel by viewModels {
        RecipierViewModelFactory((application as RecipierApplication).repository)
    }

    private var mRecipeDetails: Recipier? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddUpdateRecipeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        if (intent.hasExtra(Constants.RECIPE_EXTRA_DETAILS)) {
            mRecipeDetails = intent.getParcelableExtra(Constants.RECIPE_EXTRA_DETAILS)
        }

        setupActionBar()

        mRecipeDetails?.let {
            if (it.id != 0) {
                mImagePath = it.image
                Glide.with(this)
                    .load(mImagePath)
                    .centerCrop()
                    .into(mBinding.ivRecipePhotoMain)

                mBinding.etAddRecipeTitle.setText(it.title)
                mBinding.etAddRecipeType.setText(it.type)
                mBinding.etAddRecipeCategory.setText(it.category)
                mBinding.etAddRecipeIngredients.setText(it.ingredients)
                mBinding.etAddRecipeCookingTime.setText(it.cookingTime)
                mBinding.etAddRecipeDirection.setText(it.cookingDirection)
                mBinding.btnAddRecipe.text = resources.getString(R.string.update_recipe_button_text)
                mBinding.ivAddRecipePhotoButton.setImageDrawable(
                    ContextCompat.getDrawable(this, R.drawable.ic_edit)
                )
            }
        }

        mBinding.ivAddRecipePhotoButton.setOnClickListener(this)
        mBinding.etAddRecipeType.setOnClickListener(this)
        mBinding.etAddRecipeCategory.setOnClickListener(this)
        mBinding.etAddRecipeCookingTime.setOnClickListener(this)
        mBinding.btnAddRecipe.setOnClickListener(this)

    }

    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (mRecipeDetails != null && mRecipeDetails!!.id != 0) {
            supportActionBar?.let {
                it.title = resources.getString(R.string.edit_this_recipe)
            }
        } else {
            supportActionBar?.let {
                it.title = resources.getString(R.string.add_a_new_recipe)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                mBinding.ivAddRecipePhotoButton.id -> {
                    dialogCustomImageSelection()
                    return
                }
                mBinding.etAddRecipeType.id -> {
                    dialogCustomOptionSelection(
                        getString(R.string.select_an_option),
                        Constants.recipeTypes(),
                        Constants.RECIPE_TYPE
                    )
                    return
                }
                mBinding.etAddRecipeCategory.id -> {
                    dialogCustomOptionSelection(
                        getString(R.string.select_an_option),
                        Constants.recipeCategories(),
                        Constants.RECIPE_CATEGORY
                    )
                    return
                }
                mBinding.etAddRecipeCookingTime.id -> {
                    dialogCustomOptionSelection(
                        getString(R.string.select_an_option),
                        Constants.recipeCookingTime(),
                        Constants.RECIPE_COOKING_TIME
                    )
                    return
                }

                mBinding.btnAddRecipe.id -> {
                    val title =
                        mBinding.etAddRecipeTitle.text.toString().trim { it <= ' ' }
                    val type =
                        mBinding.etAddRecipeType.text.toString().trim { it <= ' ' }
                    val category =
                        mBinding.etAddRecipeCategory.text.toString().trim { it <= ' ' }
                    val ingredients =
                        mBinding.etAddRecipeIngredients.text.toString().trim { it <= ' ' }
                    val cookingTime =
                        mBinding.etAddRecipeCookingTime.text.toString().trim { it <= ' ' }
                    val cookingDirection =
                        mBinding.etAddRecipeDirection.text.toString().trim { it <= ' ' }

//                    val customErrorDrawable =
//                        ContextCompat.getDrawable(this, R.drawable.ic_form_error)
//                    customErrorDrawable?.setBounds(
//                        0,
//                        0,
//                        customErrorDrawable.intrinsicWidth,
//                        customErrorDrawable.intrinsicHeight
//                    )

                    when {
                        TextUtils.isEmpty(mImagePath) -> {
                            Toasty.error(
                                this,
                                getString(R.string.add_recipe_form_error_picture),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        TextUtils.isEmpty(title) -> {
                            Toasty.error(
                                this,
                                getString(R.string.add_recipe_form_error_title),
                                Toast.LENGTH_LONG
                            ).show()
//                            mBinding.etAddRecipeTitle.setError(
//                                getString(R.string.add_recipe_form_error_title),
//                                customErrorDrawable
//                            )
                        }
                        TextUtils.isEmpty(type) -> {
                            Toasty.error(
                                this,
                                getString(R.string.add_recipe_form_error_type),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        TextUtils.isEmpty(category) -> {
                            Toasty.error(
                                this,
                                getString(R.string.add_recipe_form_error_category),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        TextUtils.isEmpty(ingredients) -> {
                            Toasty.error(
                                this,
                                getString(R.string.add_recipe_form_error_ingredients),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        TextUtils.isEmpty(cookingTime) -> {
                            Toasty.error(
                                this,
                                getString(R.string.add_recipe_form_error_time),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        TextUtils.isEmpty(cookingDirection) -> {
                            Toasty.error(
                                this,
                                getString(R.string.add_recipe_form_error_directions),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        else -> {
                            var recipeID = 0
                            var imageSource = Constants.RECIPE_IMAGE_SOURCE_LOCAL
                            var favoriteRecipe = false

                            mRecipeDetails?.let {
                                if (it.id != 0) {
                                    recipeID = it.id
                                    imageSource = it.imageSource
                                    favoriteRecipe = it.favoriteRecipe
                                }
                            }

                            val recipeDetails = Recipier(
                                mImagePath,
                                imageSource,
                                title,
                                type,
                                category,
                                ingredients,
                                cookingTime,
                                cookingDirection,
                                favoriteRecipe,
                                recipeID
                            )

                            if (recipeID == 0) {
                                mRecipierViewModel.insert(recipeDetails)
                                Toasty.success(
                                    this,
                                    getString(R.string.recipe_data_recorded_to_database_success),
                                    Toast.LENGTH_LONG
                                ).show()
                                Log.i("Insertion", "Success")

                            } else {
                                mRecipierViewModel.update(recipeDetails)
                                val intent = Intent(this, MainActivity::class.java)
                                intent.setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK
                                            or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                )
                                startActivity(intent)
                                Toasty.success(
                                    this,
                                    getString(R.string.recipe_data_updated_in_database_success),
                                    Toast.LENGTH_LONG
                                ).show()
                                Log.i("Update", "Success")
                            }
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun dialogCustomOptionSelection(
        title: String,
        listOptions: List<String>,
        selection: String
    ) {
        mCustomDialog = Dialog(this)
        val oBinding = DialogCustomListBinding.inflate(layoutInflater)
        mCustomDialog.setContentView(oBinding.root)

        oBinding.tvDialogCustomListTitle.text = title

        oBinding.rvDialogCustomList.layoutManager = LinearLayoutManager(this)

        val adapter = ItemListAdapter(this, listOptions, null, selection)
        oBinding.rvDialogCustomList.adapter = adapter

        mCustomDialog.show()
    }

    fun selectedOption(option: String, selection: String) {
        when (selection) {
            Constants.RECIPE_TYPE -> {
                mCustomDialog.dismiss()
                mBinding.etAddRecipeType.setText(option)
            }
            Constants.RECIPE_CATEGORY -> {
                mCustomDialog.dismiss()
                mBinding.etAddRecipeCategory.setText(option)
            }
            Constants.RECIPE_COOKING_TIME -> {
                mCustomDialog.dismiss()
                mBinding.etAddRecipeCookingTime.setText(option)
            }
        }
    }

    private fun dialogCustomImageSelection() {
        val dialog = Dialog(this)
        val dBinding: DialogCustomImageSelectionBinding =
            DialogCustomImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(dBinding.root)

        dBinding.tvDialogImageSelectionCamera.setOnClickListener {

            Dexter.withContext(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
//              Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).withListener(
                object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if (report.areAllPermissionsGranted()) {
                                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                startActivityForResult(intent, CAMERA)
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        showRationalDialogForPermissions()
                    }

                }
            ).onSameThread().check()

            dialog.dismiss()
        }

        dBinding.tvDialogImageSelectionGallery.setOnClickListener {

            Dexter.withContext(this).withPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(
                object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        val galleryIntent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        startActivityForResult(galleryIntent, GALLERY)
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        Toast.makeText(
                            this@AddUpdateRecipeActivity,
                            getString(R.string.permission_to_gallery_denied),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        request: PermissionRequest?,
                        token: PermissionToken?
                    ) {
                        showRationalDialogForPermissions()
                    }


                }
            ).onSameThread().check()

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        val wrapper = ContextWrapper(applicationContext)

        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.png")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file.absolutePath
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA) {
                data?.let {
                    val thumbnail: Bitmap = data.extras!!.get("data") as Bitmap

                    //mBinding.ivRecipePhotoMain.setImageBitmap(thumbnail)  <-- educational purposes

                    Glide.with(this)
                        .load(thumbnail)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade(200))
                        .into(mBinding.ivRecipePhotoMain)

                    mImagePath = saveImageToInternalStorage(thumbnail)
                    Log.i("Image Path: ", mImagePath)

                    mBinding.ivAddRecipePhotoButton.setImageDrawable(
                        ContextCompat.getDrawable(this, R.drawable.ic_edit)
                    )
                }
            }
            if (requestCode == GALLERY) {
                data?.let {
                    val selectedPhotoUri = data.data

                    Glide.with(this)
                        .load(selectedPhotoUri)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                Log.e("TAG", "Error loading image", e)
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                resource?.let {
                                    val bitmap: Bitmap = resource.toBitmap()
                                    mImagePath = saveImageToInternalStorage(bitmap)
                                }
                                return false
                            }

                        })
                        .transition(DrawableTransitionOptions.withCrossFade(200))
                        .into(mBinding.ivRecipePhotoMain)

                    mBinding.ivAddRecipePhotoButton.setImageDrawable(
                        ContextCompat.getDrawable(this, R.drawable.ic_edit)
                    )
                }
            }

        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Cancelled", "User cancelled image selection")
        }
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this).setMessage(getString(R.string.permissions_off_dialog_text))
            .setPositiveButton(getString(R.string.go_to_settings)) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}