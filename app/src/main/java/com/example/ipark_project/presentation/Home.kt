package com.example.ipark_project.presentation

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ipark_project.buisiness.entities.Parking
import com.example.ipark_project.buisiness.viewmodels.ParkingsViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun GoogleMapScreen() {
    val alger = LatLng(36.737232, 3.086472)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(alger, 10f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun parkingItem(parking:Parking, navController: NavController) {
    var isFavorite by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .width(350.dp)
            .wrapContentHeight()
            .clip(shape = RoundedCornerShape(15.dp))
            .border(2.dp, Color.Gray, shape = RoundedCornerShape(15.dp))
            .clickable {
                navController.currentBackStackEntry?.savedStateHandle?.set(key = "parking", value = parking)
                navController.navigate(Router.ParkingScreen.route)
            },
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 7.dp, end = 15.dp, bottom = 7.dp)
        ) {
            Text(
                text = parking.name,
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = if (isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "",
                tint = Color(0xFFD52D2D),
                modifier = Modifier.clickable { isFavorite = !isFavorite }
            )
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 7.dp, end = 15.dp, bottom = 7.dp)
        ) {
            Text(
                text = parking.address,
                color = Color(0xFF999999),
                fontSize = 18.sp,
                fontWeight = FontWeight.Light
            )
        }
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 7.dp, end = 15.dp, bottom = 7.dp)
        ) {
            Text(
                text = parking.price,
                color = Color(0xFFD52D2D),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController,parkingsViewModel: ParkingsViewModel = viewModel()) {
    var destination by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        parkingsViewModel.getParkings()
    }
    val parkings by parkingsViewModel.parkings

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        SearchBar(
            query = destination,
            onQueryChange = { destination = it },
            onSearch = {
                active = false },
            active = active,
            onActiveChange = { active = it },
            placeholder = { Text(text = "Search for your destination") },
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Search, contentDescription = "", tint = Color(0xFFD52D2D))
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Clear, contentDescription = "", modifier = Modifier.clickable {
                        if (destination.isNotEmpty()) {
                            destination = ""
                        } else active = false
                    },
                    tint = Color(0xFFD52D2D)
                )
            },
            colors = SearchBarDefaults.colors(containerColor = Color.White),
            shape = SearchBarDefaults.dockedShape,
            modifier = Modifier.clip(RoundedCornerShape(21.dp))
        ) {
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(6f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            parkings?.let { parkingList ->
                parkingList.forEach { parking ->
                    parkingItem(
                        parking=parking,
                        navController = navController
                    )
                }
            }

        }
        //GoogleMapScreen()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .weight(1f)
                ,
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sign In buttons
            Box(
                modifier = Modifier
                    .size(120.dp, 50.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(Color(0xFFD52D2D)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Map view", fontSize = 15.sp, color = Color.White)
            }
            Box(
                modifier = Modifier
                    .size(120.dp, 50.dp)
                    .clip(shape = RoundedCornerShape(15.dp))
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Detailed view", fontSize = 15.sp, color = Color.White)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
fun testHome() {
    val navController = rememberNavController()
    HomePage(navController =navController )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

        GoogleMapScreen()

}