package com.example.mobilneprojekat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.mobilneprojekat.ui.navigation.AppNavHostWithViewModel
import com.example.mobilneprojekat.ui.theme.MobilneProjekatTheme
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilneprojekat.viewmodel.VehicleRegistrationViewModel
import com.example.mobilneprojekat.viewmodel.VehicleRegistrationViewModelFactory
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            MobilneProjekatTheme {
                val token = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIyMTkzIiwibmJmIjoxNzUxMzcyMTE4LCJleHAiOjE3NTE0NTg1MTgsImlhdCI6MTc1MTM3MjExOH0.zOG-AhmMQD18kdVnaoDQllX5_uTlHdCEdHtmVjUFynjdviqaZXUkOjgXT4KGZrm69Y8XckCZEmfypCjiEZoAEA"
                val context = LocalContext.current
                val viewModel: VehicleRegistrationViewModel = viewModel(
                    factory = VehicleRegistrationViewModelFactory(context, token)
                )
                AppNavHostWithViewModel(viewModel)
            }
        }
    }
} 