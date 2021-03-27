package com.amartgar.recipier.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amartgar.recipier.data.model.entities.Recipier
import com.amartgar.recipier.data.repository.RecipierRepository
import kotlinx.coroutines.launch

class RecipierViewModel(private val repository: RecipierRepository) : ViewModel() {

    fun insert(recipe: Recipier) = viewModelScope.launch {
        repository.insertRecipeData(recipe)
    }
}

class RecipierViewModelFactory(private val repository: RecipierRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipierViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipierViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}