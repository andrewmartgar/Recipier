package com.amartgar.recipier.viewmodel

import androidx.lifecycle.*
import com.amartgar.recipier.data.model.entities.Recipier
import com.amartgar.recipier.data.repository.RecipierRepository
import kotlinx.coroutines.launch

class RecipierViewModel(private val repository: RecipierRepository) : ViewModel() {

    fun insert(recipe: Recipier) = viewModelScope.launch {
        repository.insertRecipeData(recipe)
    }

    fun update(recipe: Recipier) = viewModelScope.launch {
        repository.updateRecipeData(recipe)
    }

    val allRecipesList: LiveData<List<Recipier>> = repository.allRecipesList.asLiveData()

    val allFavoritesList: LiveData<List<Recipier>> = repository.allFavoritesList.asLiveData()
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