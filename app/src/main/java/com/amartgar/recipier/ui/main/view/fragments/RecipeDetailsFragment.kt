package com.amartgar.recipier.ui.main.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.amartgar.recipier.R
import com.amartgar.recipier.application.RecipierApplication
import com.amartgar.recipier.databinding.FragmentRecipeDetailsBinding
import com.amartgar.recipier.ui.main.view.activities.AddUpdateRecipeActivity
import com.amartgar.recipier.utils.Constants
import com.amartgar.recipier.utils.DataDeleter
import com.amartgar.recipier.utils.DataLoader
import com.amartgar.recipier.utils.FragmentToPopulate
import com.amartgar.recipier.viewmodel.RecipierViewModel
import com.amartgar.recipier.viewmodel.RecipierViewModelFactory
import es.dmoral.toasty.Toasty

class RecipeDetailsFragment : Fragment() {

    private var mBinding: FragmentRecipeDetailsBinding? = null

    private val mRecipierViewModel: RecipierViewModel by viewModels {
        RecipierViewModelFactory(((requireActivity().application) as RecipierApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: RecipeDetailsFragmentArgs by navArgs()

        val data = FragmentToPopulate(
            this,
            mBinding!!.ivRecipeImage,
            mBinding!!.tvRecipeTitle,
            mBinding!!.tvRecipeTypeTag,
            mBinding!!.tvRecipeCategoryTag,
            mBinding!!.tvRecipeTime,
            mBinding!!.tvRecipeIngredientsText,
            mBinding!!.tvRecipeDirectionsText,
            null,
            null,
            mBinding!!.ivAddToFavorites

        )

        DataLoader(data).loadRecipeIntoUI(args)

        mBinding!!.tvEditThisRecipe.setOnClickListener {
            val intent =
                Intent(requireActivity(), AddUpdateRecipeActivity::class.java)
            intent.putExtra(Constants.RECIPE_EXTRA_DETAILS, args.recipeDetails)
            requireActivity().startActivity(intent)
        }

        mBinding!!.tvDeleteThisRecipe.setOnClickListener {
            val toDelete = DataDeleter(this)
            toDelete.deleteThisRecipe(args.recipeDetails, mRecipierViewModel)
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