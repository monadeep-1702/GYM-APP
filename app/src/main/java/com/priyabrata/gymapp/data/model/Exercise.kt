package com.priyabrata.gymapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val muscleGroup: String,
    val category: String,
    val equipment: String,
    val defaultReps: Int = 3,
    val isTimed: Boolean = false,
    val defaultSeconds: Int = 0,
    val description: String = "",
    val imageUrl: String = "",
    val baseReps: Int = 3,   // ADD THIS - never changes
    val maxWeight: Float = 0f
)