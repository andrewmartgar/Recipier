package com.amartgar.recipier.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.amartgar.recipier.data.model.entities.Recipier
import com.amartgar.recipier.databinding.ItemAllRecipesListBinding
import com.amartgar.recipier.ui.main.view.fragments.AllRecipesFragment
import com.bumptech.glide.Glide

class ItemAllRecipesListAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<ItemAllRecipesListAdapter.ViewHolder>() {

    private var recipes: List<Recipier> = listOf()

    class ViewHolder(view: ItemAllRecipesListBinding) : RecyclerView.ViewHolder(view.root) {
        val ivRecipeImage = view.ivRecipeListImage
        val tvRecipeTitle = view.tvRecipeListTitle
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemAllRecipesListAdapter.ViewHolder {
        val binding: ItemAllRecipesListBinding = ItemAllRecipesListBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemAllRecipesListAdapter.ViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.tvRecipeTitle.text = recipe.title
        Glide.with(fragment)
            .load(recipe.image)
            .into(holder.ivRecipeImage)

        holder.itemView.setOnClickListener {
            if (fragment is AllRecipesFragment) {
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