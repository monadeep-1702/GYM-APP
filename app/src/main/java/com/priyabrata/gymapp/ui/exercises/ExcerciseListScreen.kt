package com.priyabrata.gymapp.ui.exercises

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.priyabrata.gymapp.data.model.Exercise
import com.priyabrata.gymapp.ui.theme.*
import com.priyabrata.gymapp.viewmodel.GymViewModel
import kotlinx.coroutines.delay

@Composable
fun ExerciseListScreen(
    muscleGroup: String,
    equipment: String,
    navController: NavController,
    viewModel: GymViewModel = hiltViewModel()
) {
    val exercises by remember(muscleGroup, equipment) {
        when (equipment) {
            "ABS" -> viewModel.getByCategory("ABS")
            "YOGA" -> viewModel.getByCategory("YOGA")
            "STRETCHING" -> viewModel.getByCategory("STRETCHING")
            "POWER" -> viewModel.getByCategory("POWER")
            else -> viewModel.getByMuscleAndEquipment(muscleGroup, equipment)
        }
    }.collectAsState(initial = emptyList())

    val titleText = when (equipment) {
        "ABS" -> "ABS Training"
        "YOGA" -> "Yoga"
        "STRETCHING" -> "Stretching"
        "POWER" -> "Power Workouts"
        else -> muscleGroup.lowercase().replaceFirstChar { it.uppercase() }
    }

    val subtitleText = when (equipment) {
        "ABS" -> "Build a strong core"
        "YOGA" -> "Surya Namaskar & more"
        "STRETCHING" -> "Flexibility & recovery"
        "POWER" -> "Push-ups, squats & home exercises"
        else -> equipment.lowercase().replaceFirstChar { it.uppercase() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            titleText,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            subtitleText,
            color = TextSecondary,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (exercises.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No exercises found",
                    color = TextSecondary
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(exercises) { exercise ->
                    if (exercise.isTimed) {
                        TimedExerciseCard(exercise = exercise, viewModel = viewModel)
                    } else {
                        SetsExerciseCard(exercise = exercise, viewModel = viewModel)
                    }
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

val exerciseNameStyle = TextStyle(
    fontSize = 22.sp,
    fontWeight = FontWeight.ExtraBold,
    color = TextPrimary,
    shadow = Shadow(
        color = Color.Black,
        offset = Offset(0f, 0f),
        blurRadius = 12f
    )
)

val exerciseSubTextStyle = TextStyle(
    fontSize = 13.sp,
    fontWeight = FontWeight.SemiBold,
    color = TextPrimary,
    shadow = Shadow(
        color = Color.Black,
        offset = Offset(0f, 0f),
        blurRadius = 10f
    )
)

@Composable
fun SetsExerciseCard(exercise: Exercise, viewModel: GymViewModel) {
    val todayLogs by viewModel.getTodayLogs(exercise.id).collectAsState(initial = emptyList())
    val todayCount = todayLogs.size
    var sessionMax by remember(exercise.id) { mutableIntStateOf(exercise.defaultReps) }
    val maxSets = sessionMax
    val isMaxed = todayCount > maxSets
    val maxWeight by viewModel.getMaxWeight(exercise.id).collectAsState(initial = 0f)
    var currentWeight by remember(exercise.id) { mutableStateOf("") }

    LaunchedEffect(todayCount, maxSets) {
        if (todayCount > maxSets) {
            viewModel.updateDefaultSets(exercise.id, todayCount)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            if (exercise.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = exercise.imageUrl,
                    contentDescription = exercise.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop,
                    onError = { error ->
                        Log.e(
                            "GIF_LOAD",
                            "FAILED: ${exercise.name} | URL: ${exercise.imageUrl} | Error: ${error.result.throwable.message}"
                        )
                    },
                    onSuccess = {
                        Log.d("GIF_LOAD", "SUCCESS: ${exercise.name}")
                    },
                    onLoading = {
                        Log.d("GIF_LOAD", "LOADING: ${exercise.name} | URL: ${exercise.imageUrl}")
                    }
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CardBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.FitnessCenter,
                        contentDescription = exercise.name,
                        modifier = Modifier.size(80.dp),
                        tint = AccentCyan.copy(alpha = 0.3f)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.15f),
                                Color.Black.copy(alpha = 0.75f)
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = exercise.name,
                    style = exerciseNameStyle
                )

                Column {
                    if (todayLogs.isNotEmpty()) {
                        todayLogs.forEachIndexed { index, _ ->
                            Text(
                                "Set ${index + 1} - completed",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary.copy(alpha = 0.85f),
                                modifier = Modifier.padding(vertical = 1.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    LinearProgressIndicator(
                        progress = { (todayCount.toFloat() / maxSets).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = if (isMaxed) AccentRed else AccentCyan,
                        trackColor = TextSecondary.copy(alpha = 0.3f),
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "$todayCount / ${exercise.defaultReps} Sets",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                "Max $maxSets",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        // Weight input field
                        OutlinedTextField(
                            value = currentWeight,
                            onValueChange = { currentWeight = it },
                            modifier = Modifier
                                .width(90.dp)
                                .height(52.dp),
                            placeholder = {
                                Text(
                                    text = if (maxWeight > 0f) "${maxWeight.toInt()} kg" else "0 kg",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    style = TextStyle(
                                        platformStyle = PlatformTextStyle(includeFontPadding = true)
                                    )
                                )
                            },
                            textStyle = TextStyle(
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = TextSecondary,
                                focusedBorderColor = AccentCyan,
                                cursorColor = AccentCyan,
                                unfocusedPlaceholderColor = TextSecondary,
                                focusedPlaceholderColor = TextSecondary
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )

                        IconButton(
                            onClick = {
                                val weight = currentWeight.toFloatOrNull() ?: 0f
                                viewModel.logDone(
                                    exerciseId = exercise.id,
                                    muscleGroup = exercise.muscleGroup,
                                    reps = exercise.defaultReps,
                                    seconds = exercise.defaultSeconds,
                                    weight = weight
                                )
                                currentWeight = ""
                            },
                            enabled = !isMaxed,
                            modifier = Modifier
                                .size(52.dp)
                                .background(
                                    color = if (isMaxed) TextSecondary.copy(alpha = 0.2f)
                                    else AccentCyan,
                                    shape = CircleShape
                                )
                                .border(
                                    width = 2.dp,
                                    color = if (isMaxed) TextSecondary.copy(alpha = 0.3f)
                                    else AccentCyan.copy(alpha = 0.3f),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Done",
                                tint = if (isMaxed) TextSecondary.copy(alpha = 0.4f)
                                else DarkBg1,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TimedExerciseCard(exercise: Exercise, viewModel: GymViewModel) {
    val todayLogs by viewModel.getTodayLogs(exercise.id).collectAsState(initial = emptyList())

    var isRunning by remember { mutableStateOf(false) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (isRunning) {
                delay(1000L)
                elapsedSeconds++
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            if (exercise.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = exercise.imageUrl,
                    contentDescription = exercise.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop,
                    onError = { error ->
                        Log.e(
                            "GIF_LOAD",
                            "FAILED: ${exercise.name} | URL: ${exercise.imageUrl} | Error: ${error.result.throwable.message}"
                        )
                    },
                    onSuccess = {
                        Log.d("GIF_LOAD", "SUCCESS: ${exercise.name}")
                    },
                    onLoading = {
                        Log.d("GIF_LOAD", "LOADING: ${exercise.name} | URL: ${exercise.imageUrl}")
                    }
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CardBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.FitnessCenter,
                        contentDescription = exercise.name,
                        modifier = Modifier.size(80.dp),
                        tint = AccentCyan.copy(alpha = 0.3f)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.15f),
                                Color.Black.copy(alpha = 0.75f)
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = exercise.name,
                        style = exerciseNameStyle
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Target: ${exercise.defaultSeconds}s",
                        style = exerciseSubTextStyle
                    )
                }

                if (isRunning || elapsedSeconds > 0) {
                    val minutes = elapsedSeconds / 60
                    val secs = elapsedSeconds % 60
                    Text(
                        text = String.format("%02d:%02d", minutes, secs),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isRunning) Color(0xFF69F0AE) else TextPrimary,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(2f, 2f),
                                blurRadius = 10f
                            )
                        ),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                Column {
                    if (todayLogs.isNotEmpty()) {
                        todayLogs.forEachIndexed { index, log ->
                            val m = log.durationSeconds / 60
                            val s = log.durationSeconds % 60
                            Text(
                                "Set ${index + 1} - ${String.format("%02d:%02d", m, s)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary.copy(alpha = 0.85f),
                                modifier = Modifier.padding(vertical = 1.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Target: ${exercise.defaultSeconds}s",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            if (todayLogs.isNotEmpty()) {
                                Text(
                                    "${todayLogs.size} sets done",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                if (!isRunning) {
                                    elapsedSeconds = 0
                                    isRunning = true
                                } else {
                                    isRunning = false
                                    viewModel.logTimedSet(
                                        exerciseId = exercise.id,
                                        muscleGroup = exercise.muscleGroup,
                                        seconds = elapsedSeconds
                                    )
                                    if (elapsedSeconds > exercise.defaultSeconds) {
                                        viewModel.updateDefaultSeconds(
                                            exercise.id, elapsedSeconds
                                        )
                                    }
                                    elapsedSeconds = 0
                                }
                            },
                            modifier = Modifier
                                .size(52.dp)
                                .background(
                                    color = if (isRunning) AccentRed
                                    else AccentCyan,
                                    shape = CircleShape
                                )
                                .border(
                                    width = 2.dp,
                                    color = if (isRunning) TextPrimary.copy(alpha = 0.3f)
                                    else AccentCyan.copy(alpha = 0.3f),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = if (isRunning) Icons.Default.Pause
                                else Icons.Default.PlayArrow,
                                contentDescription = if (isRunning) "Stop" else "Start",
                                tint = if (isRunning) TextPrimary else DarkBg1,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}