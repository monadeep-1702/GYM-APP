package com.priyabrata.gymapp.ui.home

import com.priyabrata.gymapp.ui.theme.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Gym Buddy",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = TextPrimary  // ← changed
        )
        Text(
            "Your personal workout companion",
            color = TextSecondary  // ← changed
        )

        Spacer(modifier = Modifier.height(24.dp))

        HomeCategoryCard(
            title = "Meditation",
            subtitle = "Relax with guided audio & music",
            imageUrl = "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=400",
            onClick = { navController.navigate("meditation") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        HomeCategoryCard(
            title = "ABS Training",
            subtitle = "Build a strong core",
            imageUrl = "https://images.unsplash.com/photo-1571019614242-c5c5dee9f50b?w=400",
            onClick = { navController.navigate("exercises/ALL/ABS") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        HomeCategoryCard(
            title = "Yoga",
            subtitle = "Surya Namaskar & more",
            imageUrl = "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=400",
            onClick = { navController.navigate("exercises/ALL/YOGA") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        HomeCategoryCard(
            title = "Stretching",
            subtitle = "Flexibility & recovery",
            imageUrl = "https://images.unsplash.com/photo-1518611012118-696072aa579a?w=400",
            onClick = { navController.navigate("exercises/ALL/STRETCHING") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        HomeCategoryCard(
            title = "Power Workouts",
            subtitle = "Push-ups, squats & home exercises",
            imageUrl = "https://images.unsplash.com/photo-1598971639058-fab3c3109a00?w=400",
            onClick = { navController.navigate("exercises/ALL/POWER") }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeCategoryCard(
    title: String,
    subtitle: String,
    imageUrl: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black.copy(alpha = 0.4f)
            ) {}
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
            ) {
                // ⚠️ KEEP Color.White here — it's on dark image overlay, not on app background
                Text(title, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(subtitle, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            }
        }
    }
}