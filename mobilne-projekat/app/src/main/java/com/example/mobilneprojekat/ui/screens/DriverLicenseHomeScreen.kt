package com.example.mobilneprojekat.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobilneprojekat.ui.navigation.Routes
import com.example.mobilneprojekat.viewmodel.ValidDriverLicenseViewModel
import com.example.mobilneprojekat.data.local.ValidDriverLicenseRequestEntity
import androidx.compose.foundation.clickable

@Composable
fun DriverLicenseHomeScreen(navController: NavController, viewModel: ValidDriverLicenseViewModel) {
    val licenses by viewModel.licenses.collectAsState()
    val filter by viewModel.filter.collectAsState()
    val sortBy by viewModel.sortBy.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refresh(20)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vozačke dozvole") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Routes.ONBOARDING) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Onboarding")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh(20); isRefreshing = true }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Osvježi")
                    }
                    IconButton(onClick = { navController.navigate(Routes.DRIVER_LICENSE_FAVORITES) }) {
                        Icon(Icons.Default.Favorite, contentDescription = "Favoriti")
                    }
                    IconButton(onClick = { navController.navigate(Routes.DRIVER_LICENSE_CHART) }) {
                        Icon(Icons.Default.Favorite, contentDescription = "Grafikon")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                TextField(
                    value = filter,
                    onValueChange = { viewModel.setFilter(it) },
                    label = { Text("Pretraga (općina/kanton)") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                var expanded by remember { mutableStateOf(false) }
                Box {
                    Button(onClick = { expanded = true }) {
                        Text("Sortiraj po: $sortBy")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(onClick = { viewModel.setSortBy("maleTotal"); expanded = false }) {
                            Text("Muški")
                        }
                        DropdownMenuItem(onClick = { viewModel.setSortBy("femaleTotal"); expanded = false }) {
                            Text("Ženski")
                        }
                    }
                }
            }
            Divider()
            if (licenses.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("Nema podataka.")
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(licenses) { item ->
                        DriverLicenseListItem(item = item, onClick = {
                            navController.navigate("driver_license_details/${item.id}")
                        }, onFavorite = { isFav ->
                            viewModel.setFavorite(item.id, isFav)
                        })
                        Divider()
                    }
                }
            }
            if (error != null) {
                Snackbar(
                    action = {
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text("Zatvori")
                        }
                    },
                    modifier = Modifier.padding(8.dp)
                ) { Text(error ?: "") }
            }
        }
    }
    if (isRefreshing) {
        LaunchedEffect(Unit) {
            viewModel.refresh(20)
            isRefreshing = false
        }
    }
}

@Composable
fun DriverLicenseListItem(
    item: ValidDriverLicenseRequestEntity,
    onClick: () -> Unit,
    onFavorite: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("${item.municipality}, ${item.canton}", style = MaterialTheme.typography.subtitle1)
            Text("Institucija: ${item.institution}", style = MaterialTheme.typography.body2)
            Text("Muški: ${item.maleTotal}, Ženski: ${item.femaleTotal}", style = MaterialTheme.typography.body2)
        }
        IconButton(onClick = { onFavorite(!item.isFavorite) }) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favorit",
                tint = if (item.isFavorite) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface.copy(alpha = 0.4f)
            )
        }
    }
} 