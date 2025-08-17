package com.example.mobilneprojekat.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobilneprojekat.viewmodel.VehicleRegistrationViewModel
import com.example.mobilneprojekat.data.local.VehicleRegistrationRequestEntity
import androidx.compose.runtime.collectAsState
import androidx.compose.material.TopAppBar

@Composable
fun DetailsScreen(
    navController: NavController,
    viewModel: VehicleRegistrationViewModel,
    id: Int
) {
    val registrations by viewModel.registrations.collectAsState()
    val item = registrations.find { it.id == id }
    val context = LocalContext.current

    if (item == null) {
        Text("Podatak nije pronađen.", modifier = Modifier.padding(16.dp))
        return
    }

    Column {
        TopAppBar(
            title = { Text("Detalji") },
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
        Text("Godina: ${item.year}")
        Text("Mjesec: ${item.month}")
        Text("Datum ažuriranja: ${item.dateUpdate}")
        Text("Mjesto registracije: ${item.registrationPlace}")
        Text("Prva registracija: ${item.firstTimeRequestsTotal}")
        Text("Obnova: ${item.renewalRequestsTotal}")
        Text("Promjena vlasništva: ${item.ownershipChangesTotal}")
        Text("Deregistracija: ${item.deregisteredTotal}")
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
                val shareText = "${item.municipality}, ${item.canton} (${item.year}/${item.month})\n" +
                        "Prva registracija: ${item.firstTimeRequestsTotal}, Obnova: ${item.renewalRequestsTotal}, " +
                        "Promjena vlasništva: ${item.ownershipChangesTotal}, Deregistracija: ${item.deregisteredTotal}"
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