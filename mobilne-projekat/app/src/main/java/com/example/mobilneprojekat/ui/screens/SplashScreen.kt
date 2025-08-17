package com.example.mobilneprojekat.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.example.mobilneprojekat.ui.navigation.Routes

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(true) {
        delay(1500)
        navController.navigate(Routes.ONBOARDING) {
            popUpTo(Routes.SPLASH) { inclusive = true }
        }
    }

} 