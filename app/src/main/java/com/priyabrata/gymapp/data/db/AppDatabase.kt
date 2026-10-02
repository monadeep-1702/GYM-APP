package com.priyabrata.gymapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.priyabrata.gymapp.data.model.Exercise
import com.priyabrata.gymapp.data.model.WorkoutLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Exercise::class, WorkoutLog::class],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE exercises ADD COLUMN maxWeight REAL NOT NULL DEFAULT 0")
            }
        }

        fun create(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gym_buddy_db"
                )
                    .addMigrations(MIGRATION_6_7)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                CoroutineScope(Dispatchers.IO).launch {
                    val dao = instance.exerciseDao()
                    if (dao.getExerciseCount() == 0) {
                        dao.insertAll(getSeedExercises())
                    }
                }
                instance
            }
        }


        fun getSeedExercises(): List<Exercise> = listOf(

            // ── CHEST ── (no changes needed)
            Exercise(
                name = "Bench Press",
                muscleGroup = "CHEST",
                category = "GYM",
                equipment = "BARBELL",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Barbell-Bench-Press.gif"
            ),
            Exercise(
                name = "Dumbbell Pullover",
                muscleGroup = "CHEST",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Dumbbell-Pullover.gif"
            ),
            Exercise(
                name = "Incline Barbell Bench Press",
                muscleGroup = "CHEST",
                category = "GYM",
                equipment = "BARBELL",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Incline-Barbell-Bench-Press.gif"
            ),
            Exercise(
                name = "Dumbbell Fly",
                muscleGroup = "CHEST",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Dumbbell-Fly.gif"
            ),
            Exercise(
                name = "Dumbbell Bench Press",
                muscleGroup = "CHEST",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Dumbbell-Press.gif"
            ),
            Exercise(
                name = "Single-Arm Cable Crossover",
                muscleGroup = "CHEST",
                category = "GYM",
                equipment = "CABLE",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/10/Single-Arm-Cable-Crossover.gif"
            ),
            Exercise(
                name = "Incline Dumbbell Fly",
                muscleGroup = "CHEST",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Incline-dumbbell-Fly.gif"
            ),
            Exercise(
                name = "Incline Dumbbell Press",
                muscleGroup = "CHEST",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Incline-Dumbbell-Press.gif"
            ),
            Exercise(
                name = "Machine Fly",
                muscleGroup = "CHEST",
                category = "GYM",
                equipment = "MACHINE",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/05/10301301-Lever-Pec-Deck-Fly_Chest_720.gif"
            ),
            Exercise(
                name = "Barbell Pullover",
                muscleGroup = "CHEST",
                category = "GYM",
                equipment = "BARBELL",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/04/Barbell-Bent-Arm-Pullover.gif"
            ),
            Exercise(
                name = "Smith Machine Bench Press",
                muscleGroup = "CHEST",
                category = "GYM",
                equipment = "MACHINE",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/06/Smith-Machine-Bench-Press.gif"
            ),
            Exercise(
                name = "Reverse Grip Dumbbell Bench Press",
                muscleGroup = "CHEST",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/06/Reverse-Grip-Dumbbell-Bench-Press.gif"
            ),
            Exercise(
                name = "Single Dumbbell Close-grip Press",
                muscleGroup = "CHEST",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/10/Single-Dumbbell-Close-grip-Press.gif"
            ),
            Exercise(
                name = "Dumbbell Pullover On Stability Ball",
                muscleGroup = "CHEST",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/08/Dumbbell-Pullover-On-Stability-Ball.gif"
            ),

            // ── BACK ──
            Exercise(
                name = "Cable Rear Pulldown",
                muscleGroup = "BACK",
                category = "GYM",
                equipment = "CABLE",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/08/Cable-Rear-Pulldown.gif"
            ),
            Exercise(
                name = "Lat Pulldown",
                muscleGroup = "BACK",
                category = "GYM",
                equipment = "CABLE",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Lat-Pulldown.gif"
            ),
            Exercise(
                name = "Seated Cable Row",
                muscleGroup = "BACK",
                category = "GYM",
                equipment = "CABLE",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Seated-Cable-Row.gif"
            ),
            Exercise(
                name = "Barbell Bent Over Row",
                muscleGroup = "BACK",
                category = "GYM",
                equipment = "BARBELL",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Barbell-Bent-Over-Row.gif"
            ),
            Exercise(
                name = "Dumbbell Row",
                muscleGroup = "BACK",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Dumbbell-Row.gif"
            ),
            Exercise(
                name = "Bent Over Dumbbell Row",
                muscleGroup = "BACK",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Bent-Over-Dumbbell-Row.gif"
            ),
            Exercise(
                name = "Deadlift",
                muscleGroup = "BACK",
                category = "GYM",
                equipment = "BARBELL",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Barbell-Deadlift.gif"
            ),
            Exercise(
                name = "Face Pull",
                muscleGroup = "BACK",
                category = "GYM",
                equipment = "CABLE",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Face-Pull.gif"
            ),
            Exercise(
                name = "Sumo Deadlift",
                muscleGroup = "BACK",
                category = "GYM",
                equipment = "BARBELL",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/04/Barbell-Sumo-Deadlift.gif"
            ),
            Exercise(
                name = "Close Grip Cable Row",
                muscleGroup = "BACK",
                category = "GYM",
                equipment = "CABLE",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/06/close-grip-cable-row.gif"
            ),
            Exercise(
                name = "Lever Reverse T-Bar Row",
                muscleGroup = "BACK",
                category = "GYM",
                equipment = "MACHINE",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/08/Lever-Reverse-T-Bar-Row.gif"
            ),

            // ── BICEPS ──
            Exercise(
                name = "Waiter Curl",
                muscleGroup = "BICEPS",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2023/09/waiter-curl.gif"
            ),
            Exercise(
                name = "Dumbbell Curl",
                muscleGroup = "BICEPS",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Dumbbell-Curl.gif"
            ),
            Exercise(
                name = "Barbell Curl",
                muscleGroup = "BICEPS",
                category = "GYM",
                equipment = "BARBELL",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Barbell-Curl.gif"
            ),
            Exercise(
                name = "Concentration Curl",
                muscleGroup = "BICEPS",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Concentration-Curl.gif"
            ),
            Exercise(
                name = "Dumbbell Preacher Curl",
                muscleGroup = "BICEPS",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Dumbbell-Preacher-Curl.gif"
            ),
            Exercise(
                name = "EZ Bar Preacher Curl",
                muscleGroup = "BICEPS",
                category = "GYM",
                equipment = "BARBELL",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Z-Bar-Preacher-Curl.gif"
            ),
            Exercise(
                name = "Hammer Curl",
                muscleGroup = "BICEPS",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Hammer-Curl.gif"
            ),
            Exercise(
                name = "Lever Preacher Curl",
                muscleGroup = "BICEPS",
                category = "GYM",
                equipment = "MACHINE",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/04/Lever-Preacher-Curl.gif"
            ),
            Exercise(
                name = "Prone Incline Barbell Curl",
                muscleGroup = "BICEPS",
                category = "GYM",
                equipment = "BARBELL",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/04/Prone-Incline-Biceps-Curl.gif"
            ),
            Exercise(
                name = "Seated Hammer Curl",
                muscleGroup = "BICEPS",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/04/Seated-Hammer-Curl.gif"
            ),
            Exercise(
                name = "Dumbbell Scott Hammer Curl",
                muscleGroup = "BICEPS",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/08/Dumbbell-Scott-Hammer-Curl.gif"
            ),
            Exercise(
                name = "Lying High Bench Barbell Curl",
                muscleGroup = "BICEPS",
                category = "GYM",
                equipment = "BARBELL",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/04/Lying-High-Bench-Barbell-Curl.gif"
            ),
            Exercise(
                name = "Cable Rope Hammer Curl",
                muscleGroup = "BICEPS",
                category = "GYM",
                equipment = "CABLE",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/06/rope-bicep-curls.gif"
            ),

            // ── TRICEPS ──
            Exercise(
                name = "Barbell JM Press",
                muscleGroup = "TRICEPS",
                category = "GYM",
                equipment = "BARBELL",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2024/12/Barbell-JM-Press.gif"
            ),
            Exercise(
                name = "Push-down",
                muscleGroup = "TRICEPS",
                category = "GYM",
                equipment = "CABLE",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Pushdown.gif"
            ),
            Exercise(
                name = "Dumbbell Kickback",
                muscleGroup = "TRICEPS",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Dumbbell-Kickback.gif"
            ),
            Exercise(
                name = "One-Arm Lying Triceps Extension",
                muscleGroup = "TRICEPS",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/04/One-Arm-Lying-Triceps-Extension.gif"
            ),
            Exercise(
                name = "Seated Dumbbell Triceps Extension",
                muscleGroup = "TRICEPS",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/06/Seated-Dumbbell-Triceps-Extension.gif"
            ),
            Exercise(
                name = "Bench Dips on Floor",
                muscleGroup = "TRICEPS",
                category = "GYM",
                equipment = "BODYWEIGHT",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Bench-Dips.gif"
            ),
            Exercise(
                name = "Seated EZ-Bar Overhead Triceps Extension",
                muscleGroup = "TRICEPS",
                category = "GYM",
                equipment = "BARBELL",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/06/Seated-EZ-Bar-Overhead-Triceps-Extension.gif"
            ),
            Exercise(
                name = "Decline Close-Grip Bench To Skull Crusher",
                muscleGroup = "TRICEPS",
                category = "GYM",
                equipment = "BARBELL",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/01/Decline-Close-Grip-Bench-To-Skull-Crusher.gif"
            ),
            Exercise(
                name = "Decline Dumbbell Triceps Extension",
                muscleGroup = "TRICEPS",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/02/Decline-Dumbbell-Triceps-Extension.gif"
            ),
            Exercise(
                name = "EZ-Bar Bent Arm Pullover",
                muscleGroup = "TRICEPS",
                category = "GYM",
                equipment = "BARBELL",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/04/Barbell-Bent-Arm-Pullover.gif"
            ),
            Exercise(
                name = "Kneeling Cable Triceps Extension",
                muscleGroup = "TRICEPS",
                category = "GYM",
                equipment = "CABLE",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/02/Kneeling-Cable-Triceps-Extension.gif"
            ),
            Exercise(
                name = "One Arm High Pulley Overhead Tricep Extension",
                muscleGroup = "TRICEPS",
                category = "GYM",
                equipment = "CABLE",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/02/Cable-One-Arm-High-Pulley-Overhead-Tricep-Extension.gif"
            ),

            // ── SHOULDER ──
            Exercise(
                name = "Seated Barbell Shoulder Press",
                muscleGroup = "SHOULDER",
                category = "GYM",
                equipment = "BARBELL",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Barbell-Shoulder-Press.gif"
            ),
            Exercise(
                name = "Standing Dumbbell Shoulder Press",
                muscleGroup = "SHOULDER",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Dumbbell-Shoulder-Press.gif"
            ),
            Exercise(
                name = "Dumbbell Lateral Raise",
                muscleGroup = "SHOULDER",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Dumbbell-Lateral-Raise.gif"
            ),
            Exercise(
                name = "Barbell Military Press",
                muscleGroup = "SHOULDER",
                category = "GYM",
                equipment = "BARBELL",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/07/Barbell-Standing-Military-Press.gif"
            ),
            Exercise(
                name = "Alternating Dumbbell Front Raise",
                muscleGroup = "SHOULDER",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/06/Alternating-Dumbbell-Front-Raise.gif"
            ),
            Exercise(
                name = "Arnold Press",
                muscleGroup = "SHOULDER",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Arnold-Press.gif"
            ),
            Exercise(
                name = "Push Press",
                muscleGroup = "SHOULDER",
                category = "GYM",
                equipment = "BARBELL",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/push-press-1.gif"
            ),
            Exercise(
                name = "Barbell Clean and Press",
                muscleGroup = "SHOULDER",
                category = "GYM",
                equipment = "BARBELL",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/04/Barbell-Clean-and-Press-.gif"
            ),

            // ── LEGS ──
            Exercise(
                name = "Smith Machine Squat",
                muscleGroup = "LEGS",
                category = "GYM",
                equipment = "MACHINE",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2024/10/smith-machine-squat.gif"
            ),
            Exercise(
                name = "Dumbbell Goblet Squat",
                muscleGroup = "LEGS",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2023/01/Dumbbell-Goblet-Squat.gif"
            ),
            Exercise(
                name = "Dumbbell Walking Lunge",
                muscleGroup = "LEGS",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Dumbbell-Lunge.gif"
            ),
            Exercise(
                name = "Barbell Squat",
                muscleGroup = "LEGS",
                category = "GYM",
                equipment = "BARBELL",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/BARBELL-SQUAT.gif"
            ),
            Exercise(
                name = "Leg Press",
                muscleGroup = "LEGS",
                category = "GYM",
                equipment = "MACHINE",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2015/11/Leg-Press.gif"
            ),
            Exercise(
                name = "Deadlift",
                muscleGroup = "LEGS",
                category = "GYM",
                equipment = "BARBELL",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Barbell-Deadlift.gif"
            ),
            Exercise(
                name = "Standing Calf Raise With Dumbbell",
                muscleGroup = "LEGS",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Dumbbell-Calf-Raise.gif"
            ),
            Exercise(
                name = "Leg Press Calf Raise",
                muscleGroup = "LEGS",
                category = "GYM",
                equipment = "MACHINE",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/05/Leg-Press-Calf-Raise.gif"
            ),
            Exercise(
                name = "Bodyweight Plie Squat",
                muscleGroup = "HIP",
                category = "GYM",
                equipment = "BODYWEIGHT",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2025/04/Bodyweight-Plie-Squat.gif"
            ),
            Exercise(
                name = "Standing Cross Leg Hamstring Stretch",
                muscleGroup = "HIP",
                category = "GYM",
                equipment = "BODYWEIGHT",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/05/Standing-Cross-Leg-Hamstring-Stretch.gif"
            ),
            Exercise(
                name = "Kettlebell Split Snatch",
                muscleGroup = "HIP",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/09/Kettlebell-Split-Snatch.gif"
            ),
            Exercise(
                name = "Kettlebell Swings",
                muscleGroup = "HIP",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/09/Kettlebell-Swings.gif"
            ),
            Exercise(
                name = "Dumbbell Glute Bridge",
                muscleGroup = "HIP",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/01/Dumbbell-Glute-Bridge.gif"
            ),
            Exercise(
                name = "Dumbbell Pull Through",
                muscleGroup = "HIP",
                category = "GYM",
                equipment = "DUMBBELLS",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/10/Dumbbell-Pull-Through.gif"
            ),
            Exercise(
                name = "Pin Squat",
                muscleGroup = "HIP",
                category = "GYM",
                equipment = "BARBELL",
                defaultReps = 3,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2023/05/Pin-Squat.gif"
            ),

            // =═════════════════════════════════════
// STRETCHING (Bodyweight)
// =═════════════════════════════════════

// ── ABS ──
            Exercise(
                name = "Double Leg Stretch",
                muscleGroup = "ABS",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/05/Double-Leg-Stretch.gif"
            ),
            Exercise(
                name = "Bhujangasana | Cobra Abdominal Stretch",
                muscleGroup = "ABS",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/06/abdominal-stretch.gif"
            ),

// ── BACK ──
            Exercise(
                name = "Sphinx Stretch",
                muscleGroup = "BACK",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/05/Sphinx-Stretch.gif"
            ),
            Exercise(
                name = "Seated Hamstring Stretch",
                muscleGroup = "BACK",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/05/Seated-Hamstring-Stretch.gif"
            ),

// ── CHEST ──
            Exercise(
                name = "Doorway Pec and Shoulder Stretch",
                muscleGroup = "CHEST",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/01/Doorway-chest-and-sshoulder-stretch.gif"
            ),
            Exercise(
                name = "Dynamic Chest Stretch",
                muscleGroup = "CHEST",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/06/Dynamic-Chest-Stretch.gif"
            ),

// ── FOREARM ──
            Exercise(
                name = "Wrist Stretch",
                muscleGroup = "FOREARM",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2023/03/Wrist-Stretch.gif"
            ),

// ── LEGS (PELVIC & HIP FOCUS) ──
            Exercise(
                name = "Seated Groin / Adductor Stretch",
                muscleGroup = "LEGS",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/05/Seated-Adductor-Groin-Stretch.gif"
            ),
            Exercise(
                name = "Kneeling Hip Flexor Stretch",
                muscleGroup = "LEGS",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/08/Kneeling-Hip-Flexor-Stretch.gif"
            ),
            Exercise(
                name = "90/90 Hip Stretch",
                muscleGroup = "LEGS",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/08/90-90-Hip-Stretch.gif"
            ),
            Exercise(
                name = "Piriformis Stretch",
                muscleGroup = "LEGS",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Piriformis-Stretch.gif"
            ),
            Exercise(
                name = "Butterfly Stretch",
                muscleGroup = "LEGS",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Butterfly-Stretch.gif"
            ),
            Exercise(
                name = "Sitting Wide Leg Adductor Stretch",
                muscleGroup = "LEGS",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/06/Sitting-Wide-Leg-Adductor-Stretch.gif"
            ),
            Exercise(
                name = "Kneeling Leg Out Adductor Stretch",
                muscleGroup = "LEGS",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/12/Kneeling-Leg-Out-Adductor-Stretch.gif"
            ),
            Exercise(
                name = "Sitting Rotation Hip Stretch",
                muscleGroup = "LEGS",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/07/Sitting-Rotation-Hip-Stretch.gif"
            ),
            Exercise(
                name = "Standing Wall Calf Stretch",
                muscleGroup = "LEGS",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/02/Standing-Wall-Calf-Stretch.gif"
            ),

// ── NECK ──
            Exercise(
                name = "Neck Rotation Stretch",
                muscleGroup = "NECK",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/07/Rotating-Neck-Stretch.gif"
            ),
            Exercise(
                name = "Side Neck Stretch",
                muscleGroup = "NECK",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/06/Side-Neck-Stretch.gif"
            ),

// ── SHOULDERS ──
            Exercise(
                name = "Across Chest Shoulder Stretch",
                muscleGroup = "SHOULDERS",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/04/Across-Chest-Shoulder-Stretch.gif"
            ),
            Exercise(
                name = "Rotator Cuff Stretch",
                muscleGroup = "SHOULDERS",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/02/Rotator-Cuff-Stretch.gif"
            ),

// ── TRICEPS ──
            Exercise(
                name = "Standing Triceps Stretch",
                muscleGroup = "TRICEPS",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/02/Triceps-Stretch.gif"
            ),

            // ═══════════════════════════════════════
// ABS TRAINING
// ═══════════════════════════════════════
            Exercise(
                name = "Standing Stomach Vacuum",
                muscleGroup = "ABS",
                category = "ABS",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2025/05/Standing-Stomach-Vacuum.gif"
            ),
            Exercise(
                name = "High Plank",
                muscleGroup = "ABS",
                category = "ABS",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2023/07/High-Plank.gif"
            ),
            Exercise(
                name = "Side Plank Hip Adduction",
                muscleGroup = "ABS",
                category = "ABS",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2025/03/Side-Plank-Hip-Adduction-Copenhagen-adduction.gif"
            ),
            Exercise(
                name = "Dead Bug",
                muscleGroup = "ABS",
                category = "ABS",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/05/Dead-Bug.gif"
            ),
            Exercise(
                name = "Mountain Climber",
                muscleGroup = "ABS",
                category = "ABS",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Mountain-climber.gif"
            ),
            Exercise(
                name = "Bicycle Crunch",
                muscleGroup = "ABS",
                category = "ABS",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Bicycle-Crunch.gif"
            ),
            Exercise(
                name = "Glute Bridge",
                muscleGroup = "ABS",
                category = "ABS",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Glute-Bridge-.gif"
            ),
            Exercise(
                name = "Leg Raises",
                muscleGroup = "ABS",
                category = "ABS",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/05/Lying-Leg-Raise.gif"
            ),
            Exercise(
                name = "Flutter Kicks",
                muscleGroup = "ABS",
                category = "ABS",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Flutter-Kicks.gif"
            ),
            Exercise(
                name = "Russian Twist",
                muscleGroup = "ABS",
                category = "ABS",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Russian-Twist.gif"
            ),

// ═══════════════════════════════════════
// YOGA (Surya Namaskar + Poses)
// ═══════════════════════════════════════
            Exercise(
                name = "Pranamasana (Prayer Pose)",
                muscleGroup = "FULL_BODY",
                category = "YOGA",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                description = "Step 1: Stand with feet together, palms joined at chest",
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/12/Prayer-Pose.gif"
            ),
            Exercise(
                name = "Hasta Uttanasana (Raised Arms)",
                muscleGroup = "BACK",
                category = "YOGA",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                description = "Step 2: Inhale, stretch arms up and arch back",
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/12/Raised-Arms-Pose.gif"
            ),
            Exercise(
                name = "Uttanasana (Forward Bend)",
                muscleGroup = "LEGS",
                category = "YOGA",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                description = "Step 3: Exhale, bend forward, touch toes",
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/12/Standing-Forward-Bend-Pose.gif"
            ),
            Exercise(
                name = "Ashwa Sanchalanasana (Lunge)",
                muscleGroup = "LEGS",
                category = "YOGA",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                description = "Step 4: Right leg back, left knee bent, look up",
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/12/Low-Lunge-Pose.gif"
            ),
            Exercise(
                name = "Dandasana (Plank Pose)",
                muscleGroup = "FULL_BODY",
                category = "YOGA",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                description = "Step 5: Both legs back, body in straight line",
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2023/07/High-Plank.gif"
            ),
            Exercise(
                name = "Ashtanga Namaskar (8 Points)",
                muscleGroup = "CHEST",
                category = "YOGA",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 20,
                description = "Step 6: Knees, chest, chin touch floor",
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/12/Eight-Limbed-Pose.gif"
            ),
            Exercise(
                name = "Bhujangasana (Cobra Pose)",
                muscleGroup = "BACK",
                category = "YOGA",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 30,
                description = "Step 7: Inhale, lift chest, arch back",
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/12/Cobra-Pose.gif"
            ),
            Exercise(
                name = "Adho Mukha Svanasana (Downward Dog)",
                muscleGroup = "FULL_BODY",
                category = "YOGA",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                description = "Step 8: Hips up, inverted V shape",
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/12/Downward-Facing-Dog-Pose.gif"
            ),
            Exercise(
                name = "Warrior Pose (Virabhadrasana)",
                muscleGroup = "LEGS",
                category = "YOGA",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/12/Warrior-I-Pose.gif"
            ),
            Exercise(
                name = "Tree Pose (Vrikshasana)",
                muscleGroup = "LEGS",
                category = "YOGA",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/12/Tree-Pose.gif"
            ),

// ═══════════════════════════════════════
// STRETCHING
// ═══════════════════════════════════════
            Exercise(
                name = "Cat Cow Pose",
                muscleGroup = "BACK",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/cat-cow.gif"
            ),
            Exercise(
                name = "Supine Spinal Twist",
                muscleGroup = "BACK",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/12/Supine-Spinal-Twist.gif"
            ),
            Exercise(
                name = "Frog Pose",
                muscleGroup = "LEGS",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/12/Frog-Pose-mandukasana.gif"
            ),
            Exercise(
                name = "Happy Baby Pose",
                muscleGroup = "LEGS",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/12/Happy-Baby-Pose.gif"
            ),
            Exercise(
                name = "Arm Circles",
                muscleGroup = "SHOULDERS",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/07/Arm-Circles_Shoulders.gif"
            ),
            Exercise(
                name = "Neck Chin Tuck",
                muscleGroup = "NECK",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/06/Chin-Tuck.gif"
            ),
            Exercise(
                name = "Wall Slides",
                muscleGroup = "SHOULDERS",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/wall-slide.gif"
            ),
            Exercise(
                name = "Cossack Squat",
                muscleGroup = "LEGS",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/05/Cossack-Squat.gif"
            ),
            Exercise(
                name = "Side Lying Clam",
                muscleGroup = "HIP",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/05/Side-Lying-Clam.gif"
            ),
            Exercise(
                name = "Pelvic Tilt",
                muscleGroup = "HIP",
                category = "STRETCHING",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/02/Pelvic-Tilt.gif"
            ),

// ═══════════════════════════════════════
// POWER WORKOUTS (Home exercises)
// ═══════════════════════════════════════
            Exercise(
                name = "Push-Up",
                muscleGroup = "CHEST",
                category = "POWER",
                equipment = "BODYWEIGHT",
                defaultReps = 15,
                baseReps = 15,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Push-Up.gif"
            ),
            Exercise(
                name = "Diamond Push-Up",
                muscleGroup = "TRICEPS",
                category = "POWER",
                equipment = "BODYWEIGHT",
                defaultReps = 10,
                baseReps = 10,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Diamond-Push-up.gif"
            ),
            Exercise(
                name = "Incline Push-Up",
                muscleGroup = "CHEST",
                category = "POWER",
                equipment = "BODYWEIGHT",
                defaultReps = 15,
                baseReps = 15,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/06/Incline-Push-Up.gif"
            ),
            Exercise(
                name = "Bodyweight Squat",
                muscleGroup = "LEGS",
                category = "POWER",
                equipment = "BODYWEIGHT",
                defaultReps = 20,
                baseReps = 20,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/05/bodyweight-squat-full-version.gif"
            ),
            Exercise(
                name = "Bodyweight Lunge",
                muscleGroup = "LEGS",
                category = "POWER",
                equipment = "BODYWEIGHT",
                defaultReps = 12,
                baseReps = 12,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2023/07/bodyweight-lunges.gif"
            ),
            Exercise(
                name = "Burpees",
                muscleGroup = "FULL_BODY",
                category = "POWER",
                equipment = "BODYWEIGHT",
                defaultReps = 10,
                baseReps = 10,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2023/08/Burpee.gif"
            ),
            Exercise(
                name = "Jump Squats",
                muscleGroup = "LEGS",
                category = "POWER",
                equipment = "BODYWEIGHT",
                defaultReps = 12,
                baseReps = 12,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Jump-Squat.gif"
            ),
            Exercise(
                name = "Bench Dips",
                muscleGroup = "TRICEPS",
                category = "POWER",
                equipment = "BODYWEIGHT",
                defaultReps = 12,
                baseReps = 12,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Bench-Dips.gif"
            ),
            Exercise(
                name = "Superman",
                muscleGroup = "BACK",
                category = "POWER",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Superman-exercise.gif"
            ),
            Exercise(
                name = "Shadow Boxing",
                muscleGroup = "FULL_BODY",
                category = "POWER",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 60,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2023/09/shadow-boxing-workout.gif"
            ),
            Exercise(
                name = "Butt Kicks",
                muscleGroup = "FULL_BODY",
                category = "POWER",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/10/Butt-Kicks.gif"
            ),
            Exercise(
                name = "Fire Hydrant",
                muscleGroup = "HIP",
                category = "POWER",
                equipment = "BODYWEIGHT",
                defaultReps = 15,
                baseReps = 15,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/02/Fire-Hydrant.gif"
            ),
            Exercise(
                name = "Standing Calf Raise",
                muscleGroup = "LEGS",
                category = "POWER",
                equipment = "BODYWEIGHT",
                defaultReps = 20,
                baseReps = 20,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2021/06/Standing-Calf-Raise.gif"
            ),
            Exercise(
                name = "Bird Dog",
                muscleGroup = "BACK",
                category = "POWER",
                equipment = "BODYWEIGHT",
                isTimed = true,
                defaultSeconds = 45,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/07/Bird-Dog.gif"
            ),
            Exercise(
                name = "Frog Pump",
                muscleGroup = "HIP",
                category = "POWER",
                equipment = "BODYWEIGHT",
                defaultReps = 15,
                baseReps = 15,
                imageUrl = "https://fitnessprogramer.com/wp-content/uploads/2022/10/Frog-Pump.gif"
            )
        )
    }
}