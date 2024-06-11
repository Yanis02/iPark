package com.example.ipark_project.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ipark_project.R
import com.example.ipark_project.buisiness.viewmodels.SignUpUserViewModel

@Composable
fun SignUp(navController: NavController, signUpUserViewModel: SignUpUserViewModel) {
    var name = remember { mutableStateOf("") }
    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var confirmPassword = remember { mutableStateOf("") }
    var location = remember { mutableStateOf("") }
    var gender = remember { mutableStateOf("") }

    val context = LocalContext.current

    if (signUpUserViewModel.error.value){
        showToast("Sign up failed", context)
        signUpUserViewModel.error.value = false
    }

    if (signUpUserViewModel.success.value){
        showToast("Sign up succeeded", context)
        signUpUserViewModel.success.value = false
        name.value = ""
        email.value = ""
        password.value = ""
        confirmPassword.value = ""
        gender.value = ""
        location.value = ""
    }
    if (signUpUserViewModel.connectionError.value){
        showToast("No connexion to the server", context)
        signUpUserViewModel.connectionError.value = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Box(modifier = Modifier.clickable { navController.popBackStack() }){
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier.padding(10.dp)
                    .size(40.dp)
            )
        }
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 40.dp, vertical = 10.dp)
                .fillMaxSize(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "sign ",
                    fontSize = 45.sp,
                    lineHeight = 70.sp,
                    style = TextStyle(color = Color(0xFFD52D2D)),
                    modifier = Modifier.alignByBaseline()
                )
                Text(
                    text = "up",
                    fontSize = 45.sp,
                    lineHeight = 70.sp,
                    modifier = Modifier.alignByBaseline()
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BorderedInputField(
                    icon = Icons.Default.Person,
                    hintText = "Name",
                    keyboardType = KeyboardType.Text,
                    text_state = name
                )
                BorderedInputField(
                    icon = Icons.Default.Email,
                    hintText = "Email",
                    keyboardType = KeyboardType.Email,
                    text_state = email
                )
                BorderedInputField(
                    icon = Icons.Default.Lock,
                    hintText = "Password",
                    keyboardType = KeyboardType.Password,
                    text_state = password
                )
                BorderedInputField(
                    icon = Icons.Default.Lock,
                    hintText = "Confirm password",
                    keyboardType = KeyboardType.Password,
                    text_state = confirmPassword
                )
                BorderedInputField(
                    icon = Icons.Default.Place,
                    hintText = "Location",
                    keyboardType = KeyboardType.Text,
                    text_state = location
                )
                GenderSelection(gender = gender)
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .background(Color(0xFFD52D2D))
                        .clickable {
                            if(name.value == ""){
                                showToast("Name mustn't be blank", context)
                            }else if(email.value == ""){
                                showToast("Email mustn't be blank", context)
                            }else if (password.value == "") {
                                showToast("Password mustn't be blank", context)
                            }
                            else if(confirmPassword.value == password.value){
                                signUpUserViewModel.signUpUser(
                                    username = name.value, email = email.value, password = password.value,
                                    gender = gender.value, location = location.value
                                )
                            }else{
                                showToast("Password confirmation is wrong", context)
                            }
                                   },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Sign Up", fontSize = 25.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun GenderSelection(gender: MutableState<String>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = if (gender.value == "M") Color.Red else Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                RadioButton(
                    selected = gender.value == "M",
                    onClick = { gender.value = "M" },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Red,
                        unselectedColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Male")
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = if (gender.value == "F") Color.Red else Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                RadioButton(
                    selected = gender.value == "F",
                    onClick = { gender.value = "F" },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Red,
                        unselectedColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Female")
            }
        }
    }
}
