package com.amartgar.recipier.ui.main.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.amartgar.recipier.R
import com.amartgar.recipier.viewmodel.RandomViewModel

class RandomFragment : Fragment() {

    private lateinit var randomViewModel: RandomViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        randomViewModel =
                ViewModelProvider(this).get(RandomViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_random, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        randomViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}