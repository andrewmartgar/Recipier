package com.amartgar.recipier.ui.main.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.amartgar.recipier.R
import com.amartgar.recipier.application.RecipierApplication
import com.amartgar.recipier.data.model.entities.Recipier
import com.amartgar.recipier.databinding.FragmentFavouritesBinding
import com.amartgar.recipier.databinding.FragmentRandomBinding
import com.amartgar.recipier.ui.main.adapter.ItemRecipesListAdapter
import com.amartgar.recipier.ui.main.view.activities.MainActivity
import com.amartgar.recipier.viewmodel.RandomViewModel
import com.amartgar.recipier.viewmodel.RecipierViewModel
import com.amartgar.recipier.viewmodel.RecipierViewModelFactory

class RandomFragment : Fragment() {

    private val mRecipierViewModel: RecipierViewModel by viewModels {
        RecipierViewModelFactory((requireActivity().application as RecipierApplication).repository)
    }

    private var _mBinding: FragmentRandomBinding? = null
    private val mBinding get() = _mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _mBinding = FragmentRandomBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    fun recipeDetails(recipeDetails: Recipier) {
        findNavController()
            .navigate(
                FavouritesFragmentDirections.navActionFromFavouritesToRecipeDetails(
                    recipeDetails
                )
            )

        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.hideBottomNavigationView()
        }
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.showBottomNavigationView()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }
}