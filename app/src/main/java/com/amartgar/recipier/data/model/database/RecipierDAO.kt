package com.amartgar.recipier.data.model.database

import androidx.room.*
import com.amartgar.recipier.data.model.entities.Recipier
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipierDAO {

    @Insert
    suspend fun insertRecipeDetails(recipe: Recipier)

    @Query("SELECT * FROM RECIPES_TABLE ORDER BY ID")
    fun getAllRecipesList(): Flow<List<Recipier>>

    @Update
    suspend fun updateRecipeDetails(recipe: Recipier)

    @Query("SELECT * FROM RECIPES_TABLE WHERE favourite = 1")
    fun getAllFavorites(): Flow<List<Recipier>>

    @Delete
    suspend fun deleteRecipe(recipe: Recipier)
}