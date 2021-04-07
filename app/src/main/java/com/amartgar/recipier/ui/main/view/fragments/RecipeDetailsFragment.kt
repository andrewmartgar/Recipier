package com.amartgar.recipier.ui.main.view.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.amartgar.recipier.R
import com.amartgar.recipier.application.RecipierApplication
import com.amartgar.recipier.databinding.FragmentRecipeDetailsBinding
import com.amartgar.recipier.ui.main.view.activities.MainActivity
import com.amartgar.recipier.viewmodel.RecipierViewModel
import com.amartgar.recipier.viewmodel.RecipierViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import es.dmoral.toasty.Toasty
import java.io.IOException
import java.util.*

class RecipeDetailsFragment : Fragment() {

    private var mBinding: FragmentRecipeDetailsBinding? = null

    private val mRecipierViewModel: RecipierViewModel by viewModels {
        RecipierViewModelFactory(((requireActivity().application) as RecipierApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: RecipeDetailsFragmentArgs by navArgs()

        args.let {
            try {
                Glide.with(requireActivity())
                    .load(it.recipeDetails.image)
                    .centerCrop()
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.e("TAG", "Error loading image")
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
                                Palette.from(resource.toBitmap())
                                    .generate() { palette ->
                                        val intColor = palette?.lightVibrantSwatch?.rgb ?: 0
                                        // (requireActivity() as MainActivity).supportActionBar?.setBackgroundDrawable(intColor.toDrawable())
                                        mBinding!!.tvRecipeTitle.setBackgroundColor(intColor)
                                    }
                            }
                            return false
                        }

                    })
                    .into(mBinding!!.ivRecipeImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            mBinding!!.tvRecipeTitle.text = it.recipeDetails.title
            mBinding!!.tvRecipeCategoryTag.text = it.recipeDetails.category
            mBinding!!.tvRecipeTypeTag.text = it.recipeDetails.type.capitalize(Locale.ROOT)
            mBinding!!.tvRecipeIngredientsText.text = it.recipeDetails.ingredients
            mBinding!!.tvRecipeDirectionsText.text = it.recipeDetails.cookingDirection
            mBinding!!.tvRecipeTime.text = it.recipeDetails.cookingTime
            (requireActivity() as MainActivity).supportActionBar?.title = it.recipeDetails.title

            if (args.recipeDetails.favoriteRecipe) {
                mBinding!!.ivAddToFavorites.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(), R.drawable.ic_favorite_selected
                    )
                )
            } else {
                mBinding!!.ivAddToFavorites.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(), R.drawable.ic_favorite_unselected
                    )
                )

            }

        }

        mBinding!!.llFavouritesButton.setOnClickListener {
            args.recipeDetails.favoriteRecipe = !args.recipeDetails.favoriteRecipe
            mRecipierViewModel.update(args.recipeDetails)

            if (args.recipeDetails.favoriteRecipe) {
                mBinding!!.ivAddToFavorites.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(), R.drawable.ic_favorite_selected
                    )
                )
                Toasty.success(
                    requireActivity(),
                    getString(R.string.add_to_favorites_toast),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                mBinding!!.ivAddToFavorites.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(), R.drawable.ic_favorite_unselected
                    )
                )
                Toasty.info(
                    requireActivity(),
                    getString(R.string.removed_from_favorites_toast),
                    Toast.LENGTH_SHORT
                ).show()

            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

}