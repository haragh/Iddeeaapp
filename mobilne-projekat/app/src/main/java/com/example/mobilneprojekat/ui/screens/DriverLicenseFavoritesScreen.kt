package com.example.mobilneprojekat.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobilneprojekat.viewmodel.ValidDriverLicenseViewModel
import com.example.mobilneprojekat.data.local.ValidDriverLicenseRequestEntity
import androidx.compose.foundation.clickable

@Composable
fun DriverLicenseFavoritesScreen(
    navController: NavController,
    viewModel: ValidDriverLicenseViewModel
) {
    val favorites by viewModel.licenses.collectAsState()
    val favoriteItems = favorites.filter { it.isFavorite }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favoriti dozvole") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Nazad")
                    }
                }
            )
        }
    ) { padding ->
        if (favoriteItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("Nema sačuvanih favorita.")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
                items(favoriteItems) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { navController.navigate("driver_license_details/${item.id}") },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("${item.municipality}, ${item.canton}", style = MaterialTheme.typography.subtitle1)
                            Text("Institucija: ${item.institution}", style = MaterialTheme.typography.body2)
                        }
                        IconButton(onClick = { viewModel.setFavorite(item.id, false) }) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Ukloni iz favorita",
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }
                    Divider()
                }
            }
        }
    }
} 