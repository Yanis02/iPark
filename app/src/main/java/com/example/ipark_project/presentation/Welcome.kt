package com.example.ipark_project.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ipark_project.R

@Composable
fun Welcome(navController: NavController){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.White
            ),
    ){
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            val painter = painterResource(id = R.drawable.ipark)
            Image(painter = painter, contentDescription = "a parking photo",
                modifier = Modifier.size(200.dp,200.dp),
            )
            Text(
                text = "Welcome to iPark!",
                color = Color.Black,
                fontSize = 40.sp
            )
            Column (
                modifier = Modifier
                    .width(300.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)



            ) {
                Text(text = "Register or logIn", color = Color(0xFF9B8989), fontSize = 20.sp)
                Box(modifier = Modifier
                    .size(300.dp, 80.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(Color(0xFFD52D2D)).
                    clickable {
                        navController.navigate(Router.RegisterScreen.route)
                    },
                    contentAlignment = Alignment.Center){
                    Text(text = "Sign Up", fontSize = 25.sp, color = Color.White)
                }
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(text = "Or", color = Color(0xFF9B8989), fontSize = 20.sp)
                }
                Box(modifier = Modifier
                    .size(300.dp, 80.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(Color.Black)
                    .clickable {
                        navController.navigate(Router.AuthScreen.route)
                    },
                    contentAlignment = Alignment.Center){
                    Text(text = "Log In", fontSize = 25.sp, color = Color.White)
                }
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(text = "By using iPark u agree to our terms and conditions ", color = Color(0xFF9B8989), textAlign = TextAlign.Center, fontSize = 20.sp)
                }
            }
        }
    }
}