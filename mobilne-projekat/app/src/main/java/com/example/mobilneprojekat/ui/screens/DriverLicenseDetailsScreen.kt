package com.example.mobilneprojekat.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobilneprojekat.viewmodel.ValidDriverLicenseViewModel
import com.example.mobilneprojekat.data.local.ValidDriverLicenseRequestEntity
import androidx.compose.runtime.collectAsState

@Composable
fun DriverLicenseDetailsScreen(
    navController: NavController,
    viewModel: ValidDriverLicenseViewModel,
    id: Int
) {
    val licenses by viewModel.licenses.collectAsState()
    val item = licenses.find { it.id == id }
    val context = LocalContext.current

    if (item == null) {
        Text("Podatak nije pronađen.", modifier = Modifier.padding(16.dp))
        return
    }

    Column {
        TopAppBar(
            title = { Text("Detalji dozvole") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Nazad")
                }
            }
        )
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("${item.municipality}, ${item.canton}", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Entitet: ${item.entity}")
            Text("Institucija: ${item.institution}")
            Text("Datum ažuriranja: ${item.dateUpdate}")
            Text("Muški: ${item.maleTotal}")
            Text("Ženski: ${item.femaleTotal}")
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                IconButton(onClick = { viewModel.setFavorite(item.id, !item.isFavorite) }) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorit",
                        tint = if (item.isFavorite) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface.copy(alpha = 0.4f)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    val shareText = "${item.municipality}, ${item.canton}\n" +
                            "Institucija: ${item.institution}, Muški: ${item.maleTotal}, Ženski: ${item.femaleTotal}"
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, shareText)
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, null))
                }) {
                    Text("Podijeli")
                }
            }
        }
    }
} 