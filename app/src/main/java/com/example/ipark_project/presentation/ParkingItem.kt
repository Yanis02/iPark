package com.example.ipark_project.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.ipark_project.buisiness.URL
import com.example.ipark_project.buisiness.entities.Parking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun parkingItem(parking: Parking, navController: NavController) {
    var isFavorite by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .width(350.dp)
            .clip(shape = RoundedCornerShape(15.dp))
            .background(Color.White)
            .wrapContentHeight()
            .clickable {
                navController.currentBackStackEntry?.savedStateHandle?.set(key = "parking", value = parking)
                navController.navigate(Router.ParkingScreen.route)
            },
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            AsyncImage(
                model = URL +parking.images[0].imgFile,
                contentScale = ContentScale.Crop,
                contentDescription = "parking photos",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = parking.name,
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Address: ${parking.address}",
                    color = Color(0xFF999999),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
                Text(
                    text = "Price: ${parking.price.toDoubleOrNull()?.toInt() ?: 0} DZD",
                    color = Color(0xFFD52D2D),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

            }
            Icon(
                imageVector = if (isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint = Color(0xFFD52D2D),
                modifier = Modifier.clickable { isFavorite = !isFavorite }
            )
        }
    }
}
