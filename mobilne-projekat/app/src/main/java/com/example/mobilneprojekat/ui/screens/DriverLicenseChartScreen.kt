package com.example.mobilneprojekat.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobilneprojekat.viewmodel.ValidDriverLicenseViewModel

@Composable
fun DriverLicenseChartScreen(
    navController: NavController,
    viewModel: ValidDriverLicenseViewModel
) {
    val licenses by viewModel.licenses.collectAsState()
    val municipalityToMale = licenses.groupBy { it.municipality }
        .mapValues { it.value.sumOf { req -> req.maleTotal } }
        .toList()
        .sortedByDescending { it.second }
        .take(10)
    val maxValue = municipalityToMale.maxOfOrNull { it.second ?: 0 } ?: 1

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Grafikon - Muški po općini") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Nazad")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (municipalityToMale.isEmpty()) {
                Text("Nema podataka za grafikon.", modifier = Modifier.padding(16.dp))
            } else {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(16.dp)) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val barWidth = size.width / (municipalityToMale.size * 2)
                        municipalityToMale.forEachIndexed { index, (municipality, value) ->
                            val left = index * 2 * barWidth + barWidth / 2
                            val top = size.height - ((value ?: 0) / maxValue.toFloat()) * size.height
                            drawRect(
                                color = Color(0xFF1976D2),
                                topLeft = Offset(left, top),
                                size = androidx.compose.ui.geometry.Size(barWidth, size.height - top)
                            )
                        }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    municipalityToMale.forEach { (municipality, _) ->
                        Text(
                            text = municipality ?: "",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
            }
        }
    }
} 