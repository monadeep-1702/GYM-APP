package com.priyabrata.gymapp.ui.equipment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.priyabrata.gymapp.ui.theme.*
import com.priyabrata.gymapp.viewmodel.GymViewModel

data class EquipmentInfo(
    val key: String,
    val displayName: String,
    val imageUrl: String
)

@Composable
fun EquipmentScreen(
    muscleGroup: String,
    navController: NavController,
    viewModel: GymViewModel = hiltViewModel()
) {
    val equipmentList by viewModel.getEquipmentForMuscleGroup(muscleGroup)
        .collectAsState(initial = emptyList())

    val equipmentImages = mapOf(
        "BARBELL" to EquipmentInfo("BARBELL", "Barbell", "https://images.unsplash.com/photo-1516481265257-97e5f4bc50d5?w=400"),
        "DUMBBELLS" to EquipmentInfo("DUMBBELLS", "Dumbbells", "https://images.unsplash.com/photo-1561729955-89357c733059?w=400"),
        "CABLE" to EquipmentInfo("CABLE", "Cable", "https://images.unsplash.com/photo-1587408951296-7aa6b378a156?w=400"),
        "MACHINE" to EquipmentInfo("MACHINE", "Machine", "https://images.unsplash.com/photo-1540497077202-7c8a3999166f?w=400"),
        "BODYWEIGHT" to EquipmentInfo("BODYWEIGHT", "Bodyweight", "https://images.unsplash.com/photo-1646239646963-b0b9be56d6b5?w=400"),
    )

    val displayName = muscleGroup.lowercase()
        .replaceFirstChar { it.uppercase() }
        .replace("_", " ")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            displayName,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = TextPrimary  // ← changed
        )
        Text(
            "Select equipment",
            color = TextSecondary,  // ← changed
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (equipmentList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AccentCyan)  // ← themed
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(equipmentList) { equipment ->
                    val info = equipmentImages[equipment]
                        ?: EquipmentInfo(
                            equipment, equipment.lowercase()
                                .replaceFirstChar { it.uppercase() }, ""
                        )

                    EquipmentCard(
                        info = info,
                        onClick = {
                            navController.navigate("exercises/${muscleGroup}/${equipment}")
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentCard(info: EquipmentInfo, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (info.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = info.imageUrl,
                    contentDescription = info.displayName,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black.copy(alpha = 0.4f)
                ) {}
            }

            Text(
                text = info.displayName,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                // Keep Color.White on dark image overlay; use TextPrimary when no image
                color = if (info.imageUrl.isNotEmpty()) Color.White else TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}