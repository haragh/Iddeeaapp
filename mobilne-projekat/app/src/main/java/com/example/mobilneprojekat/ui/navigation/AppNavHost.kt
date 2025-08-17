package com.example.mobilneprojekat.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.mobilneprojekat.ui.screens.SplashScreen
import com.example.mobilneprojekat.ui.screens.OnboardingScreen
import com.example.mobilneprojekat.ui.screens.HomeScreen
import com.example.mobilneprojekat.ui.screens.DetailsScreen
import com.example.mobilneprojekat.ui.screens.FavoritesScreen
import com.example.mobilneprojekat.ui.screens.ChartScreen
import com.example.mobilneprojekat.ui.screens.DriverLicenseHomeScreen
import com.example.mobilneprojekat.ui.screens.DriverLicenseDetailsScreen
import com.example.mobilneprojekat.ui.screens.DriverLicenseFavoritesScreen
import com.example.mobilneprojekat.ui.screens.DriverLicenseChartScreen
import com.example.mobilneprojekat.viewmodel.VehicleRegistrationViewModel
import com.example.mobilneprojekat.viewmodel.ValidDriverLicenseViewModel
import com.example.mobilneprojekat.data.repository.ValidDriverLicenseRepository
import com.example.mobilneprojekat.utils.DI
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilneprojekat.viewmodel.ValidDriverLicenseViewModelFactory

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val DETAILS = "details/{id}"
    const val FAVORITES = "favorites"
    const val CHART = "chart"
    const val DRIVER_LICENSE_HOME = "driver_license_home"
    const val DRIVER_LICENSE_DETAILS = "driver_license_details/{id}"
    const val DRIVER_LICENSE_FAVORITES = "driver_license_favorites"
    const val DRIVER_LICENSE_CHART = "driver_license_chart"
}

@Composable
fun AppNavHostWithViewModel(
    vehicleViewModel: com.example.mobilneprojekat.viewmodel.VehicleRegistrationViewModel,
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val token = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyMTkzIiwibmJmIjoxNzUxMzcyMTE4LCJleHAiOjE3NTE0NTg1MTgsImlhdCI6MTc1MTM3MjExOH0.zOG-AhmMQD18kdVnaoDQllX5_uTlHdCEdHtmVjUFynjdviqaZXUkOjgXT4KGZrm69Y8XckCZEmfypCjiEZoAEA"
    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) { SplashScreen(navController) }
        composable(Routes.ONBOARDING) { OnboardingScreen(navController) }
        composable(Routes.HOME) { HomeScreen(navController, vehicleViewModel) }
        composable(
            route = "details/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            DetailsScreen(navController, vehicleViewModel, id)
        }
        composable(Routes.FAVORITES) { FavoritesScreen(navController, vehicleViewModel) }
        composable(Routes.CHART) { ChartScreen(navController, vehicleViewModel) }
        composable(Routes.DRIVER_LICENSE_HOME) {
            val dlViewModel: ValidDriverLicenseViewModel = viewModel(
                factory = com.example.mobilneprojekat.viewmodel.ValidDriverLicenseViewModelFactory(
                    DI.provideValidDriverLicenseRepository(context, token)
                )
            )
            DriverLicenseHomeScreen(navController, dlViewModel)
        }
        composable(
            route = "driver_license_details/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            val dlViewModel: ValidDriverLicenseViewModel = viewModel(
                factory = com.example.mobilneprojekat.viewmodel.ValidDriverLicenseViewModelFactory(
                    DI.provideValidDriverLicenseRepository(context, token)
                )
            )
            DriverLicenseDetailsScreen(navController, dlViewModel, id)
        }
        composable(Routes.DRIVER_LICENSE_FAVORITES) {
            val dlViewModel: ValidDriverLicenseViewModel = viewModel(
                factory = com.example.mobilneprojekat.viewmodel.ValidDriverLicenseViewModelFactory(
                    DI.provideValidDriverLicenseRepository(context, token)
                )
            )
            DriverLicenseFavoritesScreen(navController, dlViewModel)
        }
        composable(Routes.DRIVER_LICENSE_CHART) {
            val dlViewModel: ValidDriverLicenseViewModel = viewModel(
                factory = com.example.mobilneprojekat.viewmodel.ValidDriverLicenseViewModelFactory(
                    DI.provideValidDriverLicenseRepository(context, token)
                )
            )
            DriverLicenseChartScreen(navController, dlViewModel)
        }
    }
} 