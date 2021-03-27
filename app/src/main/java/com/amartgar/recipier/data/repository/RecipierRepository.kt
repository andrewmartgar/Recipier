package com.amartgar.recipier.data.repository

import androidx.annotation.WorkerThread
import com.amartgar.recipier.data.model.database.RecipierDAO
import com.amartgar.recipier.data.model.entities.Recipier

class RecipierRepository(private val mDao: RecipierDAO) {

    @WorkerThread
    suspend fun insertRecipeData(recipe: Recipier){
        mDao.insertRecipeDetails(recipe)
    }
}