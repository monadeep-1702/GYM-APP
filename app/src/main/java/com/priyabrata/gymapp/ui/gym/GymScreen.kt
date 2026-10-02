package com.priyabrata.gymapp.ui.gym

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.priyabrata.gymapp.ui.theme.*

data class MuscleGroupItem(
    val name: String,
    val key: String,
    val imageUrl: String
)

@Composable
fun GymScreen(navController: NavController) {

    val muscleGroups = listOf(
        MuscleGroupItem("Chest", "CHEST", "https://images.unsplash.com/photo-1571019614242-c5c5dee9f50b?w=400"),
        MuscleGroupItem("Back", "BACK", "https://images.unsplash.com/photo-1603287681836-b174ce5074c2?w=400"),
        MuscleGroupItem("Biceps", "BICEPS", "https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?w=400"),
        MuscleGroupItem("Triceps", "TRICEPS", "https://images.unsplash.com/photo-1507398941214-572c25f4b1dc?w=400"),
        MuscleGroupItem("Shoulder", "SHOULDER", "https://images.unsplash.com/photo-1532029837206-abbe2b7620e3?w=400"),
        MuscleGroupItem("Legs", "LEGS", "https://images.unsplash.com/photo-1434608519344-49d77a699e1d?w=400"),
        MuscleGroupItem("Hip", "HIP", "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400"),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Gym",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = TextPrimary  // ← changed
        )
        Text(
            "Select a muscle group",
            color = TextSecondary  // ← changed
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(muscleGroups) { group ->
                MuscleGroupCard(
                    group = group,
                    onClick = {
                        navController.navigate("equipment/${group.key}")
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuscleGroupCard(group: MuscleGroupItem, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = group.imageUrl,
                contentDescription = group.name,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black.copy(alpha = 0.4f)
            ) {}
            // ⚠️ Keep Color.White — text is on dark image overlay
            Text(
                text = group.name,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}