package com.priyabrata.gymapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.priyabrata.gymapp.data.model.Exercise
import com.priyabrata.gymapp.data.model.WorkoutLog
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Query("SELECT COUNT(*) FROM exercises")
    suspend fun getExerciseCount(): Int

    @Query("SELECT DISTINCT equipment FROM exercises WHERE muscleGroup = :muscleGroup AND category = 'GYM' ORDER BY equipment")
    fun getEquipmentForMuscleGroup(muscleGroup: String): Flow<List<String>>

    @Query("SELECT * FROM exercises WHERE category = :category ORDER BY name")
    fun getByCategory(category: String): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE muscleGroup = :muscleGroup ORDER BY name")
    fun getByMuscleGroup(muscleGroup: String): Flow<List<Exercise>>

    @Query(
        """
        SELECT * FROM exercises 
        WHERE muscleGroup = :muscleGroup AND equipment = :equipment 
        ORDER BY name
    """
    )
    fun getByMuscleAndEquipment(muscleGroup: String, equipment: String): Flow<List<Exercise>>

    @Query("SELECT * FROM exercises WHERE id = :id")
    suspend fun getExerciseById(id: Int): Exercise?

    @Update
    suspend fun updateExercise(exercise: Exercise)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exercises: List<Exercise>)

    @Insert
    suspend fun logWorkout(log: WorkoutLog)

    @Query(
        """
        SELECT * FROM workout_logs 
        WHERE exerciseId = :exerciseId 
        AND completedAt >= :startOfDay
        ORDER BY completedAt ASC
    """
    )
    fun getTodayLogs(exerciseId: Int, startOfDay: Long): Flow<List<WorkoutLog>>

    @Query(
        """
        SELECT COUNT(*) FROM workout_logs 
        WHERE exerciseId = :exerciseId 
        AND completedAt >= :startOfDay
    """
    )
    fun getTodayCount(exerciseId: Int, startOfDay: Long): Flow<Int>

    @Query("SELECT DISTINCT muscleGroup FROM workout_logs WHERE completedAt >= :since")
    fun getFocusedMuscleGroups(since: Long): Flow<List<String>>

    @Query("SELECT COUNT(*) FROM exercises WHERE category = :category")
    fun getCategoryCount(category: String): Flow<Int>
    @Query(
        """
        SELECT muscleGroup, COUNT(DISTINCT exerciseId) as total FROM workout_logs 
        WHERE completedAt >= :since 
        GROUP BY muscleGroup
    """
    )
    fun getWeeklyStats(since: Long): Flow<List<MuscleGroupCount>>

    @Query("DELETE FROM workout_logs")
    suspend fun clearAllLogs()

    @Query("SELECT * FROM exercises WHERE imageUrl != '' AND imageUrl IS NOT NULL")
    suspend fun getAllForPrefetch(): List<Exercise>

    @Query("UPDATE exercises SET defaultReps = baseReps WHERE id NOT IN (SELECT exerciseId FROM workout_logs WHERE completedAt >= :startOfDay GROUP BY exerciseId HAVING COUNT(*) >= defaultReps)")
    suspend fun resetUncompletedDefaults(startOfDay: Long)

    @Query("SELECT MAX(weight) FROM workout_logs WHERE exerciseId = :exerciseId")
    fun getMaxWeight(exerciseId: Int): Flow<Float?>

    @Query("UPDATE exercises SET maxWeight = :weight WHERE id = :exerciseId AND maxWeight < :weight")
    suspend fun updateMaxWeight(exerciseId: Int, weight: Float)

    @Query("SELECT maxWeight FROM exercises WHERE id = :exerciseId")
    fun getExerciseMaxWeight(exerciseId: Int): Flow<Float>
}


data class MuscleGroupCount(
    val muscleGroup: String,
    val total: Int
)