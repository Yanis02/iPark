package com.example.ipark_project.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ipark_project.R
import com.example.ipark_project.buisiness.viewmodels.SignInUserViewModel

@Composable
fun BorderedInputField(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    hintText: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    text_state : MutableState<String>
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(width = 1.dp, color = Color.Red)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(8.dp)
            )
            BasicTextField(
                value = text_state.value,
                onValueChange = { it -> text_state.value = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                singleLine = true,
                maxLines = 1,
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.CenterStart
                    ) {
                        innerTextField()
                        if (text_state.value.isEmpty()) {
                            Text(
                                text = hintText,
                                color = Color.Gray
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun RedUnderlinedText(text: String, modifier: Modifier = Modifier, fontSize: Int = 10) {
    Text(
        text = text,
        fontSize = fontSize.sp,
        color = Color.Red,
        modifier = modifier.clickable {  }
    )
}

fun showToast(message: String, context: Context) {
    // Show toast message
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

private fun saveTokenToSharedPreferences(token: String, context: Context) {
    // Get SharedPreferences instance
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    // Edit SharedPreferences to store token
    val editor = sharedPreferences.edit()
    editor.putString("token", token)
    editor.apply()
}

@Composable
fun SignIn(navController: NavController, signInUserViewModel: SignInUserViewModel){
    var username = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    val context = LocalContext.current

    if (signInUserViewModel.error.value){
        showToast("Sign In failed", context)
        signInUserViewModel.error.value = false
    }

    if (signInUserViewModel.success.value){
        showToast("Sign In succeeded", context)
        signInUserViewModel.success.value = false
        signInUserViewModel.signInResponse.value?.let { saveTokenToSharedPreferences(it.token, context) }
        username.value = ""
        password.value = ""
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
        Column (
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 40.dp, vertical = 10.dp)
                .fillMaxSize(),
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "sign " ,
                    fontSize = 45.sp,
                    lineHeight = 70.sp,
                    style = TextStyle(color = Color(0xFFD52D2D)),
                    modifier = Modifier.alignByBaseline()
                )
                Text(
                    text = "in",
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
                    hintText = "Username",
                    keyboardType = KeyboardType.Email,
                    text_state = username
                )
                BorderedInputField(
                    icon = Icons.Default.Lock,
                    hintText = "Password",
                    keyboardType = KeyboardType.Password,
                    text_state = password
                )
                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    RedUnderlinedText(
                        text = "Forgot Password?"
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(Color(0xFFD52D2D))
                    .clickable {
                        if(username.value == ""){
                            showToast("Username mustn't be blank", context)
                        }else if (password.value == ""){
                            showToast("password mustn't be blank", context)
                        }else {
                            signInUserViewModel.signInUser(
                                username = username.value, password = password.value
                            )
                        }
                    },
                    contentAlignment = Alignment.Center){
                    Text(text = "Sign In", fontSize = 25.sp, color = Color.White)
                }
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(Color(0xFFF4F4F4))
                    .clickable { },
                    contentAlignment = Alignment.Center){
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.google),
                            contentDescription = "Google Icon",
                            modifier = Modifier.size(24.dp) // Adjust size as needed
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Sign with Google", fontSize = 18.sp)
                    }
                }
            }

        }
    }
}