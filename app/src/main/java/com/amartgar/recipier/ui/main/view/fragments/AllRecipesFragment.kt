package com.amartgar.recipier.ui.main.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.amartgar.recipier.R
import com.amartgar.recipier.application.RecipierApplication
import com.amartgar.recipier.data.model.entities.Recipier
import com.amartgar.recipier.databinding.FragmentAllRecipesBinding
import com.amartgar.recipier.ui.main.adapter.ItemRecipesListAdapter
import com.amartgar.recipier.ui.main.view.activities.AddUpdateRecipeActivity
import com.amartgar.recipier.ui.main.view.activities.MainActivity
import com.amartgar.recipier.viewmodel.RecipierViewModel
import com.amartgar.recipier.viewmodel.RecipierViewModelFactory

class AllRecipesFragment : Fragment() {

    private val mRecipierViewModel: RecipierViewModel by viewModels {
        RecipierViewModelFactory((requireActivity().application as RecipierApplication).repository)
    }

    private var _mBinding: FragmentAllRecipesBinding? = null
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
        _mBinding = FragmentAllRecipesBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    fun recipeDetails(recipeDetails: Recipier) {
        findNavController()
            .navigate(AllRecipesFragmentDirections.navActionFromAllRecipesToRecipeDetails(recipeDetails))

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_recipes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_recipe -> {
                startActivity(Intent(requireActivity(), AddUpdateRecipeActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.rvRecipeList.layoutManager = GridLayoutManager(requireActivity(), 2)
        val adapter = ItemRecipesListAdapter(this)

        mBinding.rvRecipeList.adapter = adapter

        mRecipierViewModel.allRecipesList.observe(viewLifecycleOwner) { recipes ->
            recipes.let {
                if (it.isNotEmpty()) {
                    mBinding.rvRecipeList.visibility = View.VISIBLE
                    mBinding.tvNoDishesAddedYet.visibility = View.GONE

                    adapter.recipeList(it)
                } else {
                    mBinding.rvRecipeList.visibility = View.GONE
                    mBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
                }
            }

        }

        mBinding.fabAddRecipe.setOnClickListener {
                    startActivity(Intent(requireActivity(), AddUpdateRecipeActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _mBinding = null
    }
}