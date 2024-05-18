package com.example.ipark_project.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ipark_project.MyApplication
import com.example.ipark_project.buisiness.viewmodels.SignInUserViewModel
import com.example.ipark_project.buisiness.viewmodels.SignUpUserViewModel
import com.example.ipark_project.ui.theme.IPark_ProjectTheme

class MainActivity : ComponentActivity() {
    private val signUpUserViewModel: SignUpUserViewModel by viewModels {
        SignUpUserViewModel.Factory((application as MyApplication).userRepository)
    }
    private val signInUserViewModel: SignInUserViewModel by viewModels {
        SignInUserViewModel.Factory((application as MyApplication).userRepository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IPark_ProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Routing
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Router.LandingScreen.route
                    ) {
                        composable(route = Router.LandingScreen.route) {
                            Landing(navController = navController)
                        }
                        composable(route = Router.WelcomeScreen.route) {
                            Welcome(navController = navController)
                        }
                        composable(route = Router.AuthScreen.route) {
                            SignIn(navController = navController, signInUserViewModel = signInUserViewModel)
                        }
                        composable(route = Router.RegisterScreen.route) {
                            SignUp(navController = navController, signUpUserViewModel = signUpUserViewModel)
                        }
                    }


                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    IPark_ProjectTheme {
        Greeting("Android")
    }
}