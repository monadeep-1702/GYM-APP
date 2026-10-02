package com.priyabrata.gymapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.priyabrata.gymapp.ui.equipment.EquipmentScreen
import com.priyabrata.gymapp.ui.exercises.ExerciseListScreen
import com.priyabrata.gymapp.ui.gym.GymScreen
import com.priyabrata.gymapp.ui.home.HomeScreen
import com.priyabrata.gymapp.ui.log.LogScreen
import com.priyabrata.gymapp.ui.meditation.MeditationScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = currentRoute == "home",
                    onClick = {
                        navController.navigate("home") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.FitnessCenter, contentDescription = "Gym") },
                    label = { Text("Gym") },
                    selected = currentRoute == "gym",
                    onClick = {
                        navController.navigate("gym") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true

                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.ListAlt, contentDescription = "Log") },
                    label = { Text("Log") },
                    selected = currentRoute == "log",
                    onClick = {
                        navController.navigate("log") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
            composable("home") { HomeScreen(navController) }
            composable("meditation") {
                MeditationScreen(onBack = { navController.popBackStack() })
            }
            composable("gym") { GymScreen(navController) }
            composable("log") { LogScreen() }

            composable("equipment/{muscleGroup}") { backStackEntry ->
                val muscleGroup = backStackEntry.arguments?.getString("muscleGroup") ?: ""
                EquipmentScreen(
                    muscleGroup = muscleGroup,
                    navController = navController
                )
            }

            composable("exercises/{muscleGroup}/{equipment}") { backStackEntry ->
                val muscleGroup = backStackEntry.arguments?.getString("muscleGroup") ?: ""
                val equipment = backStackEntry.arguments?.getString("equipment") ?: ""
                ExerciseListScreen(
                    muscleGroup = muscleGroup,
                    equipment = equipment,
                    navController = navController
                )
            }
        }
    }
}