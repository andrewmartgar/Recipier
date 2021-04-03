package com.amartgar.recipier.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RandomViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "A random recipe will show up here!"
    }
    val text: LiveData<String> = _text
}