package com.example.mobilneprojekat.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobilneprojekat.viewmodel.VehicleRegistrationViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@Composable
fun ChartScreen(
    navController: NavController,
    viewModel: VehicleRegistrationViewModel
) {
    val registrations by viewModel.registrations.collectAsState()
    val monthToTotal = registrations.groupBy { it.month }
        .mapValues { it.value.sumOf { req -> req.firstTimeRequestsTotal } }
        .toSortedMap()
    val entries = monthToTotal.entries.map { it.key to it.value }
    val maxValue = entries.maxOfOrNull { it.second } ?: 1

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Grafikon - Prve registracije po mjesecima") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Nazad")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (entries.isEmpty()) {
                Text("Nema podataka za grafikon.", modifier = Modifier.padding(16.dp))
            } else {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(16.dp)) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val barWidth = size.width / (entries.size * 2)
                        entries.forEachIndexed { index, (month, value) ->
                            val left = index * 2 * barWidth + barWidth / 2
                            val top = size.height - (value / maxValue.toFloat()) * size.height
                            drawRect(
                                color = Color(0xFF1976D2),
                                topLeft = Offset(left, top),
                                size = androidx.compose.ui.geometry.Size(barWidth, size.height - top)
                            )
                        }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    entries.forEach { (month, _) ->
                        Text(
                            text = month.toString(),
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
            }
        }
    }
} 