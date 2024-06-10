package com.example.ipark_project.presentation

import android.content.Context
import android.location.Geocoder
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import com.example.ipark_project.buisiness.entities.Parking
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleMapScreen( parkings: List<Parking>,navController: NavController) {
    var selectedParking by remember { mutableStateOf<Parking?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var isMap by remember { mutableStateOf(true) }
    var searchResultCoordinates by remember { mutableStateOf<LatLng?>(LatLng(36.737232, 3.086472)) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val singapore = LatLng(36.737232, 3.086472)
    var cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment =Alignment.TopStart) {
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = {

                coroutineScope.launch {
                    searchResultCoordinates =searchLocation(context, searchQuery, cameraPositionState,)
                }
                active = false
            },
            active = active,
            onActiveChange = { active = it },
            placeholder = { androidx.compose.material3.Text(text = "Search for your destination") },
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
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = {
                selectedParking = null
            }
        ) {
            println(parkings+"map")
            parkings.forEach { spot ->
                Marker(
                    position = LatLng(spot.latitude.toDouble(), spot.longitude.toDouble()),
                    title = spot.name,
                    snippet = spot.price,
                    onClick = {
                        selectedParking = spot
                        true
                    }
                )
            }
        }

        selectedParking?.let { parking ->
            Column (modifier = Modifier.fillMaxSize().padding(20.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally){
                parkingItem(parking,navController)

            }
        }

    }
}



