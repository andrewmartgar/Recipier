package com.amartgar.recipier.ui.main.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.amartgar.recipier.R
import com.amartgar.recipier.databinding.FragmentAllRecipesBinding
import com.amartgar.recipier.ui.main.view.activities.AddUpdateRecipeActivity
import com.amartgar.recipier.viewmodel.AllRecipesViewModel

class AllRecipesFragment : Fragment() {

    private lateinit var allRecipesViewModel: AllRecipesViewModel

    private var _mBinding : FragmentAllRecipesBinding? = null
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
        _mBinding = FragmentAllRecipesBinding.inflate(inflater,container,false)
        val view = mBinding.root

        allRecipesViewModel =
                ViewModelProvider(this).get(AllRecipesViewModel::class.java)
        val textView: TextView = mBinding.textHome
        allRecipesViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_recipes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_recipe -> {
                startActivity(Intent(requireActivity(), AddUpdateRecipeActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
          super.onDestroyView()
        _mBinding = null
    }
}