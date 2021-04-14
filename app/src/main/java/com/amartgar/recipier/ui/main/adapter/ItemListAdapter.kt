package com.amartgar.recipier.ui.main.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.amartgar.recipier.databinding.ItemListBinding
import com.amartgar.recipier.ui.main.view.activities.AddUpdateRecipeActivity
import com.amartgar.recipier.ui.main.view.fragments.AllRecipesFragment

class ItemListAdapter(
    private val activity: Activity,
    private val listItems: List<String>,
    private val fragment: Fragment?,
    private val selection: String
) : RecyclerView.Adapter<ItemListAdapter.ViewHolder>() {

    class ViewHolder(view: ItemListBinding) : RecyclerView.ViewHolder(view.root) {
        val tvText = view.tvItemListTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemListBinding =
            ItemListBinding.inflate(LayoutInflater.from(activity), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItems[position]
        holder.tvText.text = item

        holder.itemView.setOnClickListener {
            if (activity is AddUpdateRecipeActivity) {
                activity.selectedOption(item, selection)
            }
            if (fragment is AllRecipesFragment) {

                val filter = fragment.mDataFilterer
                filter.filterSelection(
                    item,
                    fragment.mRecipierViewModel,
                    fragment.mBinding.rvRecipeList,
                    fragment.mBinding.llNoDishesAddedYet,
                    fragment.mBinding.llNoFilterResults
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

}