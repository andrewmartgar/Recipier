package com.amartgar.recipier.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.amartgar.recipier.R
import com.amartgar.recipier.data.model.entities.Recipier
import com.amartgar.recipier.ui.main.view.activities.MainActivity
import com.amartgar.recipier.ui.main.view.fragments.RecipeDetailsFragment
import com.amartgar.recipier.viewmodel.RecipierViewModel
import es.dmoral.toasty.Toasty

class DeleteRecipe(
    private val fragment: Fragment,
    private val viewModel: RecipierViewModel
) {


    fun deleteThisRecipe(recipe: Recipier) {
        val builder = AlertDialog.Builder(fragment.requireActivity())
        builder.setIcon(R.drawable.ic_alert)
        builder.setTitle(R.string.erase_recipe_dialog_title)
        builder.setMessage(fragment.requireActivity().getString(R.string.erase_recipe_dialog_body_message))
        builder.setPositiveButton(fragment.requireActivity().getString(R.string.erase_recipe_dialog_positive_button))
        { dialogInterface, _ ->
            val reBuilder = AlertDialog.Builder(fragment.requireActivity())
            reBuilder.setIcon(R.drawable.ic_alert)
            reBuilder.setTitle(fragment.requireActivity().getString(R.string.erase_recipe_dialog_title))
            reBuilder.setMessage(fragment.requireActivity().getString(R.string.erase_recipe_sub_dialog_body_message))
            reBuilder.setPositiveButton(fragment.requireActivity().getString(R.string.erase_recipe_sub_dialog_positive_button))
            { subDialogInterface, _ ->
                viewModel.delete(recipe)
                Toasty.info(
                    fragment.requireActivity(),
                    fragment.requireActivity().getString(R.string.recipe_erased_toast),
                    Toast.LENGTH_SHORT
                ).show()
                subDialogInterface.dismiss()
                dialogInterface.dismiss()
                if (fragment is RecipeDetailsFragment) {
                    val intent = Intent(fragment.requireActivity(), MainActivity::class.java)
                    intent.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK
                                or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    )
                    startActivity(fragment.requireActivity(), intent, null)
                }
            }
            reBuilder.setNegativeButton(fragment.requireActivity().getString(R.string.erase_recipe_sub_dialog_negative_button))
            { subDialogInterface, _ ->
                subDialogInterface.dismiss()
                dialogInterface.dismiss()
            }
            val subAlertDialog: AlertDialog = reBuilder.create()
            subAlertDialog.setCancelable(false)
            subAlertDialog.show()
        }
        builder.setNegativeButton(fragment.requireActivity().getString(R.string.erase_recipe_dialog_negative_button))
        { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}