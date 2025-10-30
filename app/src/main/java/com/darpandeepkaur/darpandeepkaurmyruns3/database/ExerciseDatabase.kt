package com.darpandeepkaur.darpandeepkaurmyruns3.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ExerciseEntry::class], version = 1)
abstract class ExerciseDatabase : RoomDatabase() {

    // Connects to the DAO
    abstract fun exerciseDao(): ExerciseDao

    companion object {
        // Single instance creates only one database instance for the app
        @Volatile
        private var INSTANCE: ExerciseDatabase? = null

        fun getInstance(context: Context): ExerciseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExerciseDatabase::class.java,
                    "exercise_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
