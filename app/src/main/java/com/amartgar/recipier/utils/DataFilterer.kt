package com.amartgar.recipier.utils

import android.app.Dialog
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amartgar.recipier.R
import com.amartgar.recipier.databinding.DialogCustomListBinding
import com.amartgar.recipier.ui.main.adapter.ItemListAdapter
import com.amartgar.recipier.ui.main.adapter.ItemRecipesListAdapter
import com.amartgar.recipier.viewmodel.RecipierViewModel

class DataFilterer(context: Fragment) {

    private val mContext = context
    private val mCustomListDialog = Dialog(context.requireActivity())

    fun filterHere() {
        val mBinding: DialogCustomListBinding =
            DialogCustomListBinding.inflate(mContext.layoutInflater)
        mCustomListDialog.setContentView(mBinding.root)
        mCustomListDialog

        mBinding.tvDialogCustomListTitle.text = mContext.getString(R.string.filter_options_title)
        val recipeCategories = Constants.recipeCategories()
        recipeCategories.add(0, Constants.ALL_ITEMS)
        mBinding.rvDialogCustomList.layoutManager = LinearLayoutManager(mContext.requireActivity())

        val adapter =
            ItemListAdapter(
                mContext.requireActivity(),
                recipeCategories,
                mContext,
                Constants.FILTER_SELECTION
            )

        mBinding.rvDialogCustomList.adapter = adapter
        mCustomListDialog.show()
    }

    fun filterSelection(
        filterItemSelection: String,
        viewModel: RecipierViewModel,
        recyclerView: RecyclerView,
        noRecipesView: View,
        noFilterResult: View
    ) {
        mCustomListDialog.dismiss()

        recyclerView.layoutManager =
            GridLayoutManager(mContext.requireActivity(), 2)

        val adapter = ItemRecipesListAdapter(mContext)
        recyclerView.adapter = adapter

        if (filterItemSelection == Constants.ALL_ITEMS) {
            viewModel.allRecipesList.observe(mContext.viewLifecycleOwner) { recipes ->
                recipes.let {
                    if (it.isNotEmpty()) {
                        recyclerView.visibility = View.VISIBLE
                        noRecipesView.visibility = View.GONE
                        noFilterResult.visibility = View.GONE

                        adapter.recipeList(it)
                    } else {
                        recyclerView.visibility = View.GONE
                        noRecipesView.visibility = View.VISIBLE
                        noFilterResult.visibility = View.GONE
                    }
                }
            }
        } else {
            viewModel.getFilteredCategoryList(filterItemSelection)
                .observe(mContext.viewLifecycleOwner) { recipes ->
                    recipes.let {
                        if (it.isNotEmpty()) {
                            recyclerView.visibility = View.VISIBLE
                            noRecipesView.visibility = View.GONE
                            noFilterResult.visibility = View.GONE

                            adapter.recipeList(it)
                        } else {
                            recyclerView.visibility = View.GONE
                            noRecipesView.visibility = View.GONE
                            noFilterResult.visibility = View.VISIBLE
                        }
                    }
                }
        }

    }
}