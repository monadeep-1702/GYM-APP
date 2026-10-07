package com.priyabrata.gymapp.ui.diet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietDetailScreen(categoryName: String, navController: NavController) {
    val dietInfo = getDietInfo(categoryName)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$categoryName Diet") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Overview",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = dietInfo.overview)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Key Recommendations",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            dietInfo.recommendations.forEach { recommendation ->
                Text(text = "• $recommendation", modifier = Modifier.padding(bottom = 4.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Sample Meal Plan",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("Breakfast: ${dietInfo.breakfast}", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Lunch: ${dietInfo.lunch}", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Dinner: ${dietInfo.dinner}", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Snacks: ${dietInfo.snacks}", fontWeight = FontWeight.SemiBold)
        }
    }
}

data class DietInfo(
    val overview: String,
    val recommendations: List<String>,
    val breakfast: String,
    val lunch: String,
    val dinner: String,
    val snacks: String
)

fun getDietInfo(category: String): DietInfo {
    return when (category) {
        "Normal" -> DietInfo(
            overview = "A balanced diet that maintains your current weight while providing all essential nutrients.",
            recommendations = listOf(
                "Eat a mix of proteins, complex carbs, and healthy fats.",
                "Stay hydrated (2-3 liters of water per day).",
                "Consume plenty of fruits and vegetables."
            ),
            breakfast = "Oatmeal with fruits and nuts, or eggs with whole-grain toast.",
            lunch = "Grilled chicken/tofu salad with quinoa or brown rice.",
            dinner = "Baked fish/paneer with steamed vegetables.",
            snacks = "Greek yogurt, nuts, or a piece of fruit."
        )
        "Obese" -> DietInfo(
            overview = "A calorie-deficit diet designed to help you lose weight safely and effectively.",
            recommendations = listOf(
                "Focus on portion control and track your calories.",
                "Avoid sugary drinks and highly processed foods.",
                "Eat protein with every meal to stay full longer."
            ),
            breakfast = "Scrambled egg whites with spinach, black coffee or green tea.",
            lunch = "Large mixed greens salad with grilled chicken breast and light vinaigrette.",
            dinner = "Baked salmon or tofu with asparagus and a small portion of sweet potato.",
            snacks = "Carrot sticks with hummus, or a small handful of almonds."
        )
        "Diabetic" -> DietInfo(
            overview = "A diet focused on managing blood sugar levels with low glycemic index foods.",
            recommendations = listOf(
                "Avoid refined carbs and added sugars.",
                "Eat regular, balanced meals to avoid blood sugar spikes.",
                "Include high-fiber foods like legumes, oats, and vegetables."
            ),
            breakfast = "Chia seed pudding made with unsweetened almond milk, or a veggie omelet.",
            lunch = "Lentil soup with a side of mixed vegetables.",
            dinner = "Grilled turkey or fish with broccoli and cauliflower rice.",
            snacks = "Apple slices with peanut butter, or roasted chickpeas."
        )
        "Skinny" -> DietInfo(
            overview = "A calorie-surplus diet rich in proteins and complex carbs to build muscle and gain healthy weight.",
            recommendations = listOf(
                "Eat in a caloric surplus (consume more calories than you burn).",
                "Consume 1.6-2.2 grams of protein per kg of body weight.",
                "Eat frequent meals and nutrient-dense foods."
            ),
            breakfast = "4 whole eggs, 2 slices of whole-wheat toast, and a glass of whole milk.",
            lunch = "Large portion of chicken breast, brown rice, and avocado.",
            dinner = "Steak or salmon with mashed potatoes and mixed veggies.",
            snacks = "Protein shake, peanut butter sandwich, or trail mix."
        )
        "Skinny Fat" -> DietInfo(
            overview = "A body recomposition diet focusing on high protein to build muscle while maintaining a slight calorie deficit or maintenance to lose fat.",
            recommendations = listOf(
                "Prioritize protein intake to support muscle growth.",
                "Lift heavy weights 3-4 times a week.",
                "Moderate your carbohydrate intake."
            ),
            breakfast = "Protein smoothie (whey protein, spinach, berries, almond milk).",
            lunch = "Turkey or chicken wrap with whole-grain tortilla and plenty of veggies.",
            dinner = "Lean beef or chicken with quinoa and roasted Brussels sprouts.",
            snacks = "Cottage cheese, hard-boiled eggs, or a protein bar."
        )
        else -> DietInfo("No information available.", listOf(), "", "", "", "")
    }
}

@Preview(showBackground = true)
@Composable
fun DietDetailScreenPreview() {
    DietDetailScreen(categoryName = "Obese", navController = rememberNavController())
}
