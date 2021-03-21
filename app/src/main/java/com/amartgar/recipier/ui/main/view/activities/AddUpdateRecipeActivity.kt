package com.amartgar.recipier.ui.main.view.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.amartgar.recipier.R
import com.amartgar.recipier.databinding.ActivityAddUpdateRecipeBinding
import com.amartgar.recipier.databinding.DialogCustomImageSelectionBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener

class AddUpdateRecipeActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val CAMERA = 1
        private const val GALLERY = 2
    }

    private lateinit var mBinding: ActivityAddUpdateRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddUpdateRecipeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupActionBar()

        mBinding.ivAddRecipePhotoButton.setOnClickListener(this)

    }

    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
                        report?.let{
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
                        val galleryIntent = Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(galleryIntent, GALLERY)
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        Toast.makeText(
                            this@AddUpdateRecipeActivity,
                            getString(R.string.permission_to_gallery_denied),
                            Toast.LENGTH_SHORT).show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == CAMERA) {
                data?.let {
                    val thumbnail: Bitmap = data.extras!!.get("data") as Bitmap
                    mBinding.ivRecipePhotoMain.setImageBitmap(thumbnail)

                    mBinding.ivAddRecipePhotoButton.setImageDrawable(
                        ContextCompat.getDrawable(this, R.drawable.ic_edit)
                    )
                }
            }
            if (requestCode == GALLERY) {
                data?.let {
                    val selectedPhotoUri = data.data
                    mBinding.ivRecipePhotoMain.setImageURI(selectedPhotoUri)

                    mBinding.ivAddRecipePhotoButton.setImageDrawable(
                        ContextCompat.getDrawable(this, R.drawable.ic_edit)
                    )
                }
            }

        }else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Cancelled", "User cancelled image selection")
        }
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this).setMessage(getString(R.string.permissions_off_dialog_text))
            .setPositiveButton(getString(R.string.go_to_settings)) {_,_ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName,null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton(getString(R.string.cancel)) {dialog,_ ->
                dialog.dismiss()
            }
            .show()
    }

}