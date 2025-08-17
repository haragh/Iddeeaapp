package com.example.mobilneprojekat.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobilneprojekat.ui.navigation.Routes

@Composable
fun OnboardingScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Dobrodošli! Odaberite dataset.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.ONBOARDING) { inclusive = true }
                }
            }) {
                Text("Registracije vozila")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                navController.navigate(Routes.DRIVER_LICENSE_HOME) {
                    popUpTo(Routes.ONBOARDING) { inclusive = true }
                }
            }) {
                Text("Vozačke dozvole")
            }
        }
    }
} 