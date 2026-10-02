package com.priyabrata.gymapp.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import coil.ImageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.priyabrata.gymapp.data.db.ExerciseDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@HiltWorker
class GifPrefetchWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val dao: ExerciseDao,
    private val imageLoader: ImageLoader
) : CoroutineWorker(context, params) {

    companion object {
        private const val TAG = "GIF_PREFETCH"
        private const val LOG_FILE = "gif_prefetch_log.txt"
    }

    override suspend fun doWork(): Result {
        val logFile = File(context.filesDir, LOG_FILE)
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        // Clear old log and start fresh
        logFile.writeText("═══════════════════════════════════════\n")
        logFile.appendText("GIF PREFETCH LOG - $timestamp\n")
        logFile.appendText("═══════════════════════════════════════\n\n")

        Log.d(TAG, "Starting GIF prefetch...")
        writeLog(logFile, "Starting GIF prefetch...")

        val exercises = dao.getAllForPrefetch()
        writeLog(logFile, "Total exercises to download: ${exercises.size}\n")

        var success = 0
        var failed = 0
        var skipped = 0
        val errors = mutableListOf<String>()

        exercises.forEachIndexed { index, exercise ->
            val url = exercise.imageUrl.trim()

            if (url.isBlank()) {
                skipped++
                return@forEachIndexed
            }

            try {
                val request = ImageRequest.Builder(context)
                    .data(url)
                    .size(400, 400)
                    .build()

                when (val result = imageLoader.execute(request)) {
                    is SuccessResult -> {
                        success++
                        val msg = "✅ [${index + 1}/${exercises.size}] ${exercise.name}"
                        Log.d(TAG, msg)
                        writeLog(logFile, msg)
                    }

                    is ErrorResult -> {
                        failed++
                        val error = result.throwable.message ?: "Unknown error"
                        val msg = "❌ [${index + 1}/${exercises.size}] ${exercise.name}\n" +
                                "   URL: $url\n" +
                                "   ERROR: $error"
                        Log.e(TAG, msg)
                        writeLog(logFile, msg)
                        errors.add("${exercise.name} → $error")
                    }
                }
            } catch (e: Exception) {
                failed++
                val msg = "❌ [${index + 1}/${exercises.size}] ${exercise.name}\n" +
                        "   URL: $url\n" +
                        "   EXCEPTION: ${e.message}"
                Log.e(TAG, msg)
                writeLog(logFile, msg)
                errors.add("${exercise.name} → ${e.message}")
            }

            // Log progress every 50 exercises
            if ((index + 1) % 50 == 0) {
                val progress = "── Progress: ${index + 1}/${exercises.size} (✅$success ❌$failed) ──"
                Log.d(TAG, progress)
                writeLog(logFile, "\n$progress\n")
            }
        }

        // ── FINAL SUMMARY ──
        val summary = """
            |
            |═══════════════════════════════════════
            |PREFETCH COMPLETE
            |═══════════════════════════════════════
            |Total:   ${exercises.size}
            |✅ Success: $success
            |❌ Failed:  $failed
            |⏭️ Skipped: $skipped
            |═══════════════════════════════════════
        """.trimMargin()

        Log.d(TAG, summary)
        writeLog(logFile, summary)

        // ── ERROR SUMMARY ──
        if (errors.isNotEmpty()) {
            writeLog(logFile, "\n\n── ALL ERRORS ──\n")
            errors.forEachIndexed { i, err ->
                writeLog(logFile, "${i + 1}. $err")
            }
        }

        writeLog(logFile, "\nLog saved at: ${logFile.absolutePath}")
        Log.d(TAG, "Log saved at: ${logFile.absolutePath}")

        return Result.success()
    }

    private fun writeLog(file: File, message: String) {
        file.appendText(message + "\n")
    }
}