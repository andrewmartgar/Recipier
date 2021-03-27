package com.amartgar.recipier.data.model.database

import androidx.room.Dao
import androidx.room.Insert
import com.amartgar.recipier.data.model.entities.Recipier

@Dao
interface RecipierDAO {

    @Insert
    suspend fun insertRecipeDetails (recipe: Recipier)
}