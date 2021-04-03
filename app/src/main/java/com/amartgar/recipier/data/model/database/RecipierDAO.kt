package com.amartgar.recipier.data.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.amartgar.recipier.data.model.entities.Recipier
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipierDAO {

    @Insert
    suspend fun insertRecipeDetails(recipe: Recipier)

    @Query("SELECT * FROM RECIPES_TABLE ORDER BY ID")
    fun getAllRecipesList(): Flow<List<Recipier>>
}