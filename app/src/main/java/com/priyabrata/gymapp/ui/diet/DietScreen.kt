package com.priyabrata.gymapp.ui.diet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.priyabrata.gymapp.ui.theme.TextPrimary
import com.priyabrata.gymapp.ui.theme.TextSecondary

data class DietCategory(val name: String, val description: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietScreen(navController: NavController) {
    val dietCategories = listOf(
        DietCategory("Normal", "Balanced diet for maintaining current weight and overall health."),
        DietCategory("Obese", "Calorie deficit diet focused on weight loss and fat reduction."),
        DietCategory("Diabetic", "Low glycemic index diet to manage blood sugar levels."),
        DietCategory("Skinny", "Calorie surplus diet rich in proteins to gain weight and muscle mass."),
        DietCategory("Skinny Fat", "High protein, moderate carb diet to lose fat and gain muscle simultaneously.")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Diet Preferences",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = "Select a category to view recommendations",
            color = TextSecondary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(dietCategories) { category ->
                Card(
                    onClick = { navController.navigate("dietDetail/${category.name}") },
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = category.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = category.description,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DietScreenPreview() {
    DietScreen(navController = rememberNavController())
}
