package com.amartgar.recipier.ui.main.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.amartgar.recipier.R
import com.amartgar.recipier.data.model.entities.Recipier
import com.amartgar.recipier.databinding.ItemAllRecipesListBinding
import com.amartgar.recipier.ui.main.view.activities.AddUpdateRecipeActivity
import com.amartgar.recipier.ui.main.view.fragments.AllRecipesFragment
import com.amartgar.recipier.ui.main.view.fragments.FavouritesFragment
import com.amartgar.recipier.utils.Constants
import com.bumptech.glide.Glide

class ItemRecipesListAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<ItemRecipesListAdapter.ViewHolder>() {

    private var recipes: List<Recipier> = listOf()

    class ViewHolder(view: ItemAllRecipesListBinding) : RecyclerView.ViewHolder(view.root) {
        val ivRecipeImage = view.ivRecipeListImage
        val tvRecipeTitle = view.tvRecipeListTitle
        val llRecipeFavorite = view.llFavourites
        val ivMore = view.ivMore
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemRecipesListAdapter.ViewHolder {
        val binding: ItemAllRecipesListBinding = ItemAllRecipesListBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemRecipesListAdapter.ViewHolder, position: Int) {
        val recipe = recipes[position]

        holder.tvRecipeTitle.text = recipe.title

        // Favorite icon
        if (recipe.favoriteRecipe && fragment is AllRecipesFragment) {
            holder.llRecipeFavorite.visibility = View.VISIBLE
        } else {
            holder.llRecipeFavorite.visibility = View.GONE
        }

        // More menu
        if (fragment is AllRecipesFragment) {
            holder.ivMore.visibility = View.VISIBLE
        } else {
            holder.ivMore.visibility = View.GONE
        }

        holder.ivMore.setOnClickListener {
            val popupMenu = PopupMenu(fragment.context, holder.ivMore)
            popupMenu.menuInflater.inflate(R.menu.menu_adapter, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener {
                if (it.itemId == R.id.action_edit_recipe) {
                    val intent =
                        Intent(fragment.requireActivity(), AddUpdateRecipeActivity::class.java)
                    intent.putExtra(Constants.RECIPE_EXTRA_DETAILS, recipe)
                    fragment.requireActivity().startActivity(intent)

                } else if (it.itemId == R.id.action_delete_recipe) {
                    if (fragment is AllRecipesFragment) {
                        fragment.deleteRecipe(recipe)
                    }

                }
                true
            }
            popupMenu.show()
        }

        // Image
        Glide.with(fragment)
            .load(recipe.image)
            .into(holder.ivRecipeImage)

        holder.itemView.setOnClickListener {
            if (fragment is AllRecipesFragment) {
                fragment.recipeDetails(recipe)
            } else if (fragment is FavouritesFragment) {
                fragment.recipeDetails(recipe)
            }
        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun recipeList(list: List<Recipier>) {
        recipes = list
        notifyDataSetChanged()
    }
}