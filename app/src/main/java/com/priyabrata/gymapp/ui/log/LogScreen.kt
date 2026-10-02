package com.priyabrata.gymapp.ui.log

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.priyabrata.gymapp.viewmodel.GymViewModel

data class MuscleLogInfo(
    val key: String,
    val displayName: String,
    val imageUrl: String
)

@Composable
fun LogScreen(viewModel: GymViewModel = hiltViewModel()) {

    val focusedGroups by viewModel.getAllFocusedMuscleGroups()
        .collectAsState(initial = emptyList())

    val stats by viewModel.getAllWeeklyStats()
        .collectAsState(initial = emptyList())

    val muscleLogItems = listOf(
        MuscleLogInfo(
            "CHEST", "Chest",
            "https://images.unsplash.com/photo-1571019614242-c5c5dee9f50b?w=400"
        ),
        MuscleLogInfo(
            "BACK", "Back",
            "https://images.unsplash.com/photo-1603287681836-b174ce5074c2?w=400"
        ),
        MuscleLogInfo(
            "BICEPS", "Biceps",
            "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?w=400"
        ),
        MuscleLogInfo(
            "TRICEPS", "Triceps",
            "https://images.unsplash.com/photo-1507398941214-572c25f4b1dc?w=400"
        ),
        MuscleLogInfo(
            "SHOULDER", "Shoulder",
            "https://images.unsplash.com/photo-1532029837206-abbe2b7620e3?w=400"
        ),
        MuscleLogInfo(
            "LEGS", "Legs",
            "https://images.unsplash.com/photo-1434608519344-49d77a699e1d?w=400"
        ),
        MuscleLogInfo(
            "HIP", "Hip",
            "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400"
        ),
        MuscleLogInfo(
            "FULL_BODY", "Full Body",
            "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=400"
        ),
    )

    var showResetDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Log",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Track your muscle group focus",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Summary card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "${focusedGroups.size}",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text("Focused", fontSize = 12.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "${muscleLogItems.size - focusedGroups.size}",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                    Text("Unfocused", fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Muscle group list with photos
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(muscleLogItems) { muscle ->
                val isFocused = focusedGroups.contains(muscle.key)
                val count = stats.find { it.muscleGroup == muscle.key }?.total ?: 0

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Background image - same as gym screen
                        AsyncImage(
                            model = muscle.imageUrl,
                            contentDescription = muscle.displayName,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )

                        // Overlay - green tint if focused, red tint if not
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = if (isFocused)
                                Color(0xFF4CAF50).copy(alpha = 0.55f)
                            else
                                Color(0xFFF44336).copy(alpha = 0.55f)
                        ) {}

                        // Content
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isFocused)
                                        Icons.Default.CheckCircle
                                    else
                                        Icons.Default.Cancel,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        muscle.displayName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        if (isFocused) "Focused" else "Unfocused",
                                        fontSize = 11.sp,
                                        color = Color.White.copy(alpha = 0.8f)
                                    )
                                }
                            }

                            if (isFocused && count > 0) {
                                Text(
                                    "$count exercises",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Reset button
        Button(
            onClick = { showResetDialog = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(Icons.Default.DeleteForever, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Reset Log")
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Log?") },
            text = { Text("This will clear all your workout data. Are you sure?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetWeeklyLog()
                        showResetDialog = false
                    }
                ) {
                    Text("Reset", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}