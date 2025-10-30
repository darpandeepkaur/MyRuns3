package com.darpandeepkaur.darpandeepkaurmyruns3.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ExerciseDao {

    // Insert a new exercise entry
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: ExerciseEntry): Long

    // Delete an existing exercise entry
    @Delete
    suspend fun delete(entry: ExerciseEntry)

    // Get all entries from newest to oldest
    @Query("SELECT * FROM exercise_entries ORDER BY id DESC")
    suspend fun getAllEntries(): List<ExerciseEntry>

    // Get a specific entry by ID
    @Query("SELECT * FROM exercise_entries WHERE id = :id LIMIT 1")
    suspend fun getEntryById(id: Long): ExerciseEntry
}