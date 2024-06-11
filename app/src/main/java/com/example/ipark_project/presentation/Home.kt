package com.example.ipark_project.presentation

import android.content.Context
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ipark_project.buisiness.viewmodels.ParkingsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

fun distanceBetween(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val earthRadius = 6371.0 // km
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    return earthRadius * c
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController, parkingsViewModel: ParkingsViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var isMap by remember { mutableStateOf(true) }
    var searchResultCoordinates by remember { mutableStateOf<LatLng?>(LatLng(36.737232, 3.086472)) }

    LaunchedEffect(Unit) {
        parkingsViewModel.getParkings()
    }
    val parkings by parkingsViewModel.parkings
    if(parkingsViewModel.connectionError.value){
        showToast("No connexion to the server", LocalContext.current)
        parkingsViewModel.connectionError.value = false
    }
    println(parkings)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val filteredParkings = searchResultCoordinates?.let { coords ->
        parkings?.filter { parking ->
            val distance = distanceBetween(coords.latitude, coords.longitude, parking.latitude.toDouble(), parking.longitude.toDouble())
            distance <= 5.0 // Filter parkings within 5 km radius
        }
    } ?: parkings
    val singapore = LatLng(36.737232, 3.086472)
    var cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }
    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp, 50.dp)
                        .clip(shape = RoundedCornerShape(15.dp))
                        .background(Color(0xFFD52D2D))
                        .clickable {
                            isMap = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Map view", fontSize = 15.sp, color = Color.White)
                }
                Box(
                    modifier = Modifier
                        .size(120.dp, 50.dp)
                        .clip(shape = RoundedCornerShape(15.dp))
                        .background(Color.Black)
                        .clickable {
                            isMap = false

                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Detailed view", fontSize = 15.sp, color = Color.White)
                }
            }
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).background(Color(0xFFE5E5E5))) {
                AnimatedVisibility(visible = isMap) {
                    //cameraPositionState = CameraPositionState(position = CameraPosition.fromLatLngZoom(singapore, 10f))

                    parkings?.let { GoogleMapScreen(it, navController) }

                }
                AnimatedVisibility(visible = !isMap) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 100.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        filteredParkings?.let { parkingList ->
                            parkingList.forEach { parking ->
                                parkingItem(parking = parking, navController = navController)
                            }
                        }
                    }
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onSearch = {
                            cameraPositionState = CameraPositionState(position = CameraPosition.fromLatLngZoom(singapore, 10f))

                            coroutineScope.launch {
                                searchResultCoordinates =searchLocation(context, searchQuery, cameraPositionState,)
                            }
                            active = false
                        },
                        active = active,
                        onActiveChange = { active = it },
                        placeholder = { Text(text = "Search for your destination") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Outlined.Search, contentDescription = "", tint = Color(0xFFD52D2D))
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Clear, contentDescription = "", modifier = Modifier.clickable {
                                    if (searchQuery.isNotEmpty()) {
                                        searchQuery = ""
                                    } else active = false
                                },
                                tint = Color(0xFFD52D2D)
                            )
                        },
                        colors = SearchBarDefaults.colors(containerColor = Color.White),
                        shape = SearchBarDefaults.dockedShape,
                        modifier = Modifier.clip(RoundedCornerShape(21.dp)).padding(20.dp)
                    ){}

                }

            }
        }
    )
}

suspend fun searchLocation(context: Context, query: String, cameraPositionState: CameraPositionState): LatLng? {
    var coords:LatLng?=null
    withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocationName(query, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val address = addresses[0]
                    val latLng = LatLng(address.latitude, address.longitude)
                    withContext(Dispatchers.Main) {
                        cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

                    }
                    coords=latLng
                } else {
                    // Handle case where no address is found
                    withContext(Dispatchers.Main) {
                        // Show a message to the user
                        // Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle exceptions such as network issues
            withContext(Dispatchers.Main) {
                // Show a message to the user
                // Toast.makeText(context, "Failed to find location", Toast.LENGTH_SHORT).show()

            }
        }
    }
    return coords
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun testHome() {
    val navController = rememberNavController()
    HomePage(navController =navController )
}

