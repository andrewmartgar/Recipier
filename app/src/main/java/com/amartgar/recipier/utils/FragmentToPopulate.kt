package com.amartgar.recipier.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

data class FragmentToPopulate(

    val fragment: Fragment,
    val mainImage: ImageView,
    val mainTitle: TextView,
    val typeTag: TextView,
    val categoryTag: TextView,
    val cookingTime: TextView,
    val ingredients: TextView,
    val directions: TextView,
    val saveTop: View?,
    val saveBottom: View?,
    val favorite: ImageView?
)