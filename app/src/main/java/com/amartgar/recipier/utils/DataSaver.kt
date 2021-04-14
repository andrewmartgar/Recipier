package com.amartgar.recipier.utils

import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.amartgar.recipier.R
import com.amartgar.recipier.data.model.entities.Recipier
import com.amartgar.recipier.ui.main.view.activities.MainActivity
import com.amartgar.recipier.ui.main.view.fragments.RandomFragment
import com.amartgar.recipier.viewmodel.RecipierViewModel
import es.dmoral.toasty.Toasty

class DataSaver(var data: FragmentToPopulate) {

    fun saveAndGo(recipe: Recipier, viewModel: RecipierViewModel) {

        if (data.fragment is RandomFragment) {
            viewModel.insert(recipe)
            val intent = Intent(data.fragment.requireActivity(), MainActivity::class.java)
            intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        or Intent.FLAG_ACTIVITY_CLEAR_TASK
            )
            ContextCompat.startActivity(data.fragment.requireActivity(), intent, null)
        }
        Toasty.success(
            data.fragment.requireActivity(),
            data.fragment.requireActivity().getString(R.string.recipe_saved_toast),
            Toast.LENGTH_SHORT
        ).show()
    }

}