package com.amartgar.recipier.ui.main.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.amartgar.recipier.application.RecipierApplication
import com.amartgar.recipier.databinding.FragmentRandomBinding
import com.amartgar.recipier.utils.FragmentToPopulate
import com.amartgar.recipier.viewmodel.RandomRecipeViewModel
import com.amartgar.recipier.viewmodel.RecipierViewModel
import com.amartgar.recipier.viewmodel.RecipierViewModelFactory

class RandomFragment : Fragment() {

    private lateinit var mRandomRecipeViewModel: RandomRecipeViewModel

    private var _mBinding: FragmentRandomBinding? = null
    val mBinding get() = _mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _mBinding = FragmentRandomBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = FragmentToPopulate(
            this,
            mBinding.ivRecipeImage,
            mBinding.tvRecipeTitle,
            mBinding.tvRecipeTypeTag,
            mBinding.tvRecipeCategoryTag,
            mBinding.tvRecipeTime,
            mBinding.tvRecipeIngredientsText,
            mBinding.tvRecipeDirectionsText,
            mBinding.llSaveRecipeTop,
            mBinding.llSaveRecipeBottom,
            null
        )

        val mRecipierViewModel: RecipierViewModel by viewModels {
            RecipierViewModelFactory(
                (requireActivity().application as RecipierApplication).repository
            )
        }

        mRandomRecipeViewModel = ViewModelProvider(this)
            .get(RandomRecipeViewModel()::class.java)

        mRandomRecipeViewModel.getRandomRecipe()

        mBinding.rlRefreshRandomRecipe.setOnRefreshListener {
            mRandomRecipeViewModel.getRandomRecipe()
        }

        mRandomRecipeViewModel.randomRecipeViewModelObserver(
            viewLifecycleOwner, data, mRecipierViewModel
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }
}