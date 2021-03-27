package com.amartgar.recipier.data.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amartgar.recipier.data.model.entities.Recipier

@Database(entities = [Recipier::class], version = 1)
public abstract class RecipierRoomDatabase: RoomDatabase() {

    abstract fun mDAO(): RecipierDAO

    companion object{
        @Volatile
        private var INSTANCE: RecipierRoomDatabase? = null

        fun getDatabase(context: Context): RecipierRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipierRoomDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}