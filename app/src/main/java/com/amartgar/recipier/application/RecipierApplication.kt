package com.amartgar.recipier.application

import android.app.Application
import com.amartgar.recipier.data.model.database.RecipierRoomDatabase
import com.amartgar.recipier.data.repository.RecipierRepository

class RecipierApplication: Application() {

    private val database by lazy { RecipierRoomDatabase.getDatabase(this) }

    val repository by lazy { RecipierRepository(database.mDAO())}

}