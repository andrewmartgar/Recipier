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

    val allRecipesList: Flow<List<Recipier>> = mDao.getAllRecipesList()
}