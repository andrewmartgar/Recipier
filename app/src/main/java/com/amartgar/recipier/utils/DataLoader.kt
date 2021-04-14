package com.amartgar.recipier.utils

import android.os.Build
import android.text.Html
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.amartgar.recipier.R
import com.amartgar.recipier.data.model.entities.RandomRecipier
import com.amartgar.recipier.data.model.entities.Recipier
import com.amartgar.recipier.ui.main.view.activities.MainActivity
import com.amartgar.recipier.ui.main.view.fragments.RecipeDetailsFragmentArgs
import com.amartgar.recipier.viewmodel.RecipierViewModel
import com.bumptech.glide.Glide
import es.dmoral.toasty.Toasty
import java.io.IOException
import java.util.*

class DataLoader(val data: FragmentToPopulate) {

    fun loadRandomRecipeIntoUI(recipe: RandomRecipier.Recipe?, viewModel: RecipierViewModel) {
        recipe?.let {
            Glide.with(data.fragment.requireActivity())
                .load(recipe.image)
                .centerCrop()
                .into(data.mainImage)

            data.mainTitle.text = recipe.title

            var type = ""

            if (recipe.dishTypes.isNotEmpty()) {
                val dishType = recipe.dishTypes[0]
                if (Constants.recipeTypes().contains(dishType)) {
                    data.typeTag.text = dishType.capitalize(Locale.ROOT)
                    type = dishType.capitalize(Locale.ROOT)
                } else {
                    data.typeTag.text = data.fragment.getString(R.string.other_tag)
                    type = data.fragment.getString(R.string.other_tag)
                }
            } else {
                data.typeTag.text = data.fragment.getString(R.string.other_tag)
                type = data.fragment.getString(R.string.other_tag)
            }

            var category = ""
            if (recipe.cuisines.isNotEmpty()) {
                val dishCategory = recipe.cuisines[0].toString()
                if (Constants.recipeCategories().contains(dishCategory)) {
                    data.categoryTag.text = dishCategory.capitalize(Locale.ROOT)
                    category = dishCategory.capitalize(Locale.ROOT)
                } else {
                    data.categoryTag.text = data.fragment.getString(R.string.other_tag)
                    category = data.fragment.getString(R.string.other_tag)
                }
            } else {
                data.categoryTag.text = data.fragment.getString(R.string.other_tag)
                category = data.fragment.getString(R.string.other_tag)
            }

            val cookingTime =
                when (recipe.readyInMinutes) {
                    in 0..15 -> "10-15 min"
                    in 16..20 -> "15-20 min"
                    in 21..25 -> "20-25 min"
                    in 26..30 -> "25-30 min"
                    in 31..40 -> "30-40 min"
                    in 41..50 -> "40-50 min"
                    in 51..59 -> "50-60 min"
                    in 60..65 -> "1 hour"
                    in 65..90 -> "1 to 1.5 hours"
                    in 90..120 -> "1.5 to 2 hours"
                    in 120..180 -> "2 to 3 hours"
                    else -> "more than 3 hours"
                }
            data.cookingTime.text = cookingTime

            var ingredients = ""
            for (value in recipe.extendedIngredients) {
                if (ingredients.isEmpty()) {
                    ingredients = value.original
                } else {
                    ingredients = ingredients + ", \n" + value.original
                }
            }
            data.ingredients.text = ingredients

            var cookingDirections = ""
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                data.directions.text = Html.fromHtml(
                    recipe.instructions,
                    Html.FROM_HTML_MODE_COMPACT
                )
            } else {
                @Suppress("DEPRECATION")
                data.directions.text = Html.fromHtml(recipe.instructions)
            }
            cookingDirections = data.directions.text.toString()

            val internalId = recipe.id

            val randomRecipeDetails = Recipier(
                recipe.image,
                Constants.RECIPE_IMAGE_SOURCE_ONLINE,
                recipe.title,
                type,
                category,
                ingredients,
                cookingTime,
                cookingDirections,
                false
            )

            data.saveTop?.setOnClickListener {
                if (IdChecker().internalIdList.contains(internalId)) {
                    Toasty.info(
                        data.fragment.requireActivity(),
                        data.fragment.getString(R.string.you_have_this_recipe),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    IdChecker().internalIdList.add(internalId)
                    DataSaver(data).saveAndGo(randomRecipeDetails, viewModel)
                }
            }

            data.saveBottom?.setOnClickListener {
                if (IdChecker().internalIdList.contains(internalId)) {
                    Toasty.info(
                        data.fragment.requireActivity(),
                        data.fragment.getString(R.string.you_have_this_recipe),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    IdChecker().internalIdList.add(internalId)
                    DataSaver(data).saveAndGo(randomRecipeDetails, viewModel)
                }
            }
        }


    }

    fun loadRecipeIntoUI(recipe: RecipeDetailsFragmentArgs) {

        recipe.let {
            try {
                Glide.with(data.fragment.requireActivity())
                    .load(it.recipeDetails.image)
                    .centerCrop()
                    .into(data.mainImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            data.mainTitle.text = it.recipeDetails.title
            data.categoryTag.text = it.recipeDetails.category
            data.typeTag.text = it.recipeDetails.type.capitalize(Locale.ROOT)
            data.ingredients.text = it.recipeDetails.ingredients
            data.directions.text = it.recipeDetails.cookingDirection
            data.cookingTime.text = it.recipeDetails.cookingTime
            (data.fragment.requireActivity() as MainActivity).supportActionBar?.title =
                it.recipeDetails.title

            if (it.recipeDetails.favoriteRecipe) {
                data.favorite?.setImageDrawable(
                    ContextCompat.getDrawable(
                        data.fragment.requireActivity(), R.drawable.ic_favorite_selected
                    )
                )
            } else {
                data.favorite?.setImageDrawable(
                    ContextCompat.getDrawable(
                        data.fragment.requireActivity(), R.drawable.ic_favorite_unselected
                    )
                )

            }
        }
    }

}