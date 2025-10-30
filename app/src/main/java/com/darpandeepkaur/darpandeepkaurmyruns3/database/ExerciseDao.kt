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

    // Delete an existing exercise entry by passing the full object
    @Delete
    suspend fun delete(entry: ExerciseEntry)

    // Delete an entry directly by its ID
    @Query("DELETE FROM exercise_entries WHERE id = :entryId")
    suspend fun deleteById(entryId: Long)

    // Get all entries from newest to oldest
    @Query("SELECT * FROM exercise_entries ORDER BY id DESC")
    suspend fun getAllEntries(): List<ExerciseEntry>

    // Get a specific entry by ID
    @Query("SELECT * FROM exercise_entries WHERE id = :id LIMIT 1")
    suspend fun getEntryById(id: Long): ExerciseEntry?
}