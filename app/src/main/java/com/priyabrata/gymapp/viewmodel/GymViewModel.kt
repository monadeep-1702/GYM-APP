package com.priyabrata.gymapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.priyabrata.gymapp.data.db.ExerciseDao
import com.priyabrata.gymapp.data.db.MuscleGroupCount
import com.priyabrata.gymapp.data.model.Exercise
import com.priyabrata.gymapp.data.model.WorkoutLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class GymViewModel @Inject constructor(
    private val dao: ExerciseDao
) : ViewModel() {

    private val allMuscleGroups = listOf(
        "CHEST", "BACK", "BICEPS", "TRICEPS", "SHOULDER", "LEGS", "HIP", "FULL_BODY"
    )

    fun getEquipmentForMuscleGroup(muscleGroup: String): Flow<List<String>> {
        return dao.getEquipmentForMuscleGroup(muscleGroup)
    }

    fun getByMuscleGroup(muscleGroup: String): Flow<List<Exercise>> {
        return dao.getByMuscleGroup(muscleGroup)
    }

    fun getByMuscleAndEquipment(muscleGroup: String, equipment: String): Flow<List<Exercise>> {
        return dao.getByMuscleAndEquipment(muscleGroup, equipment)
    }

    fun getByCategory(category: String): Flow<List<Exercise>> {
        return dao.getByCategory(category)
    }

    fun getTodayCount(exerciseId: Int): Flow<Int> {
        return dao.getTodayCount(exerciseId, getStartOfDay())
    }

    fun getTodayLogs(exerciseId: Int): Flow<List<WorkoutLog>> {
        return dao.getTodayLogs(exerciseId, getStartOfDay())
    }

    fun getAllFocusedMuscleGroups(): Flow<List<String>> {
        return dao.getFocusedMuscleGroups(0L)
    }

    fun getAllWeeklyStats(): Flow<List<MuscleGroupCount>> {
        return dao.getWeeklyStats(0L)
    }

    fun getAllMuscleGroups(): List<String> = allMuscleGroups

    fun getMaxWeight(exerciseId: Int): Flow<Float> {
        return dao.getExerciseMaxWeight(exerciseId)
    }

    fun logDone(exerciseId: Int, muscleGroup: String, reps: Int, seconds: Int, weight: Float = 0f) {
        viewModelScope.launch {
            dao.logWorkout(
                WorkoutLog(
                    exerciseId = exerciseId,
                    muscleGroup = muscleGroup,
                    reps = reps,
                    durationSeconds = seconds,
                    weight = weight
                )
            )
            if (weight > 0f) {
                dao.updateMaxWeight(exerciseId, weight)
            }
        }
    }

    fun logTimedSet(exerciseId: Int, muscleGroup: String, seconds: Int) {
        viewModelScope.launch {
            dao.logWorkout(
                WorkoutLog(
                    exerciseId = exerciseId,
                    muscleGroup = muscleGroup,
                    reps = 0,
                    durationSeconds = seconds
                )
            )
        }
    }

    fun updateDefaultSets(exerciseId: Int, newSets: Int) {
        viewModelScope.launch {
            val exercise = dao.getExerciseById(exerciseId)
            if (exercise != null) {
                dao.updateExercise(exercise.copy(defaultReps = newSets))
            }
        }
    }

    fun updateDefaultSeconds(exerciseId: Int, newSeconds: Int) {
        viewModelScope.launch {
            val exercise = dao.getExerciseById(exerciseId)
            if (exercise != null) {
                dao.updateExercise(exercise.copy(defaultSeconds = newSeconds))
            }
        }
    }

    fun resetWeeklyLog() {
        viewModelScope.launch {
            val startOfDay = getStartOfDay()
            dao.resetUncompletedDefaults(startOfDay)
            dao.clearAllLogs()
        }
    }

    private fun getStartOfDay(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }
}