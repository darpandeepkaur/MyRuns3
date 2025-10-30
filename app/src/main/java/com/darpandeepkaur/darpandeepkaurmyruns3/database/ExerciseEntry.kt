package com.darpandeepkaur.darpandeepkaurmyruns3.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.*
import java.util.*

@Entity(tableName = "exercise_entries")
data class ExerciseEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val inputType: Int,           // Manual, GPS, or Automatic
    val activityType: Int,        // Running, Walking, etc.
    val dateTime: Long,           // When this entry happened (stored as timestamp)
    val duration: Double,         // Exercise duration in seconds
    val distance: Double,         // Distance in meters or feet
    val avgPace: Double?,         // Average pace
    val avgSpeed: Double?,        // Average speed
    val calorie: Double?,         // Calories burned
    val climb: Double?,           // Climb in meters/feet
    val heartRate: Double?,       // Heart rate
    val comment: String?,         // User comments
    val locationList: ByteArray?  // Serialized GPS coordinates as BLOB
) {

    companion object {

        // Converts an ArrayList to a ByteArray for storage
        fun locationListToBytes(list: ArrayList<Pair<Double, Double>>): ByteArray {
            val bos = ByteArrayOutputStream()
            ObjectOutputStream(bos).use { it.writeObject(list) }
            return bos.toByteArray()
        }

        // Converts ByteArray back into ArrayList
        fun bytesToLocationList(bytes: ByteArray?): ArrayList<Pair<Double, Double>> {
            if (bytes == null) return ArrayList()
            val bis = ByteArrayInputStream(bytes)
            ObjectInputStream(bis).use {
                @Suppress("UNCHECKED_CAST")
                return it.readObject() as ArrayList<Pair<Double, Double>>
            }
        }
    }
}