package com.amartgar.recipier.ui.main.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.amartgar.recipier.databinding.FragmentRecipeDetailsBinding
import com.amartgar.recipier.ui.main.view.activities.MainActivity
import com.bumptech.glide.Glide
import java.io.IOException
import java.util.*

class RecipeDetailsFragment : Fragment() {

    private var mBinding: FragmentRecipeDetailsBinding? = null

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

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

}