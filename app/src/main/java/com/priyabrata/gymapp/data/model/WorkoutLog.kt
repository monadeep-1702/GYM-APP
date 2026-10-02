package com.priyabrata.gymapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_logs")
data class WorkoutLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val exerciseId: Int,
    val muscleGroup: String,
    val sets: Int = 1,
    val reps: Int,
    val weight: Float = 0f,
    val durationSeconds: Int = 0,
    val completedAt: Long = System.currentTimeMillis()
)