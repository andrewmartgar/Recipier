package com.amartgar.recipier.data.repository

import androidx.annotation.WorkerThread
import com.amartgar.recipier.data.model.database.RecipierDAO
import com.amartgar.recipier.data.model.entities.Recipier
import kotlinx.coroutines.flow.Flow

class RecipierRepository(private val mDao: RecipierDAO) {

    @WorkerThread
    suspend fun insertRecipeData(recipe: Recipier) {
        mDao.insertRecipeDetails(recipe)
    }

    @WorkerThread
    suspend fun updateRecipeData(recipe: Recipier) {
        mDao.updateRecipeDetails(recipe)
    }

    @WorkerThread
    suspend fun deleteRecipeData(recipe: Recipier) {
        mDao.deleteRecipe(recipe)
    }

    val allRecipesList: Flow<List<Recipier>> = mDao.getAllRecipesList()

    val allFavoritesList: Flow<List<Recipier>> = mDao.getAllFavorites()

    fun filteredCategoryList(value: String): Flow<List<Recipier>> = mDao.getFilteredCategory(value)

}