package com.amartgar.recipier.ui.main.view.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.amartgar.recipier.R
import com.amartgar.recipier.application.RecipierApplication
import com.amartgar.recipier.data.model.entities.Recipier
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
    private var mRecipeDetails: Recipier? = null
    private val mRecipierViewModel: RecipierViewModel by viewModels {
        RecipierViewModelFactory(((requireActivity().application) as RecipierApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_share, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                val type = "text/plain"
                val subject = "Checkout this recipe"
                var extraText = ""
                val shareWith = "Share with"

                mRecipeDetails?.let {
                    var image = ""
                    if (it.imageSource == Constants.RECIPE_IMAGE_SOURCE_ONLINE) {
                        image = it.image
                    }

                    var cookingDirections = ""
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        cookingDirections = Html.fromHtml(
                            it.cookingDirection,
                            Html.FROM_HTML_MODE_COMPACT
                        ).toString()
                    } else {
                        @Suppress("DEPRECATION")
                        cookingDirections = Html.fromHtml(it.cookingDirection).toString()
                    }

                    extraText =
                        "$image \n\n " +
                                "Title: ${it.title} \n\n " +
                                "Type: ${it.type} \n\n " +
                                "Category: ${it.category} \n\n " +
                                "Ingredients: ${it.ingredients} \n\n " +
                                "Instructions to cook: $cookingDirections \n\n " +
                                "Aprox. cooking time: ${it.cookingTime} \n\n "
                }

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = type
                intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                intent.putExtra(Intent.EXTRA_TEXT, extraText)
                startActivity(Intent.createChooser(intent, shareWith))

                return true
            }
        }
        return super.onOptionsItemSelected(item)
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

        mRecipeDetails = args.recipeDetails

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