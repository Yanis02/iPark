package com.example.ipark_project.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ipark_project.presentation.fonts.landingText
import com.example.ipark_project.R

@Composable
fun Landing(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFD52D2D),
                        Color(0xFF6F1717)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier

                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
           val painter = painterResource(id =R.drawable.landing)
            Box (modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(start = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(painter = painter, contentDescription = "a parking photo",
                    modifier = Modifier.fillMaxSize(), // Take the full size of its container
                    contentScale = ContentScale.FillBounds// Scale the image to fit within the bounds
                )
            }
            Row (modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "iPark, 1st parking app in Algeria",
                    color = Color.White,
                    fontSize = 50.sp,
                    lineHeight = 80.sp,
                    fontFamily = landingText(),
                )
            }
            Row (modifier = Modifier.padding(20.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.End) {
               Box(
                   modifier = Modifier.size(170.dp,60.dp)
                       .clip(shape = RoundedCornerShape(15.dp))
                       .background(Color(0xFFF5F5F5))
                       .clickable {
                                  navController.navigate(Router.WelcomeScreen.route)
                       },
                   contentAlignment = Alignment.Center




               ) {
                   Text(
                       text = "Start",
                       color = Color(0xFFD52D2D),
                       fontSize = 20.sp,


                   )
               }
            }
                  }
    }
}
