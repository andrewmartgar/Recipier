package com.amartgar.recipier.viewmodel

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.amartgar.recipier.data.repository.NetworkRepository
import com.amartgar.recipier.ui.main.view.fragments.RandomFragment
import com.amartgar.recipier.utils.DataLoader
import com.amartgar.recipier.utils.FragmentToPopulate

class RandomRecipeViewModel : ViewModel() {
    private val nRepository = NetworkRepository()
    fun getRandomRecipe() = nRepository.getRandomRecipeFromAPI()

    fun randomRecipeViewModelObserver(
        lifecycleOwner: LifecycleOwner,
        data: FragmentToPopulate,
        viewModel: RecipierViewModel
    ) {
        nRepository.randomRecipeResponse.observe(lifecycleOwner,
            { randomRecipeResponse ->
                randomRecipeResponse?.let {
                    Log.i("Random Recipe Response", "${randomRecipeResponse.recipes[0]}")

                    if (data.fragment is RandomFragment) {
                        val fragment = data.fragment
                        if (fragment.mBinding.rlRefreshRandomRecipe.isRefreshing) {
                            fragment.mBinding.rlRefreshRandomRecipe.isRefreshing = false
                        }
                    }
                    DataLoader(
                        data,
                    ).loadRandomRecipeIntoUI(randomRecipeResponse.recipes[0], viewModel)
                }
            })

        nRepository.randomRecipeLoadingError.observe(lifecycleOwner,
            { dataError ->
                dataError?.let {
                    Log.e("Random Recipe API Error", "$dataError")
                }
            })

        nRepository.loadRandomRecipe.observe(lifecycleOwner,
            { loadRandomRecipe ->
                loadRandomRecipe?.let {
                    Log.e("Random Recipe Loading", "$loadRandomRecipe")
                }
            }
        )
    }

}