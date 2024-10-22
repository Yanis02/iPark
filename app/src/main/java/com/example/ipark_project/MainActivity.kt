package com.example.ipark_project.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ipark_project.MyApplication
import com.example.ipark_project.buisiness.viewmodels.SignInUserViewModel
import com.example.ipark_project.buisiness.viewmodels.SignUpUserViewModel
import com.example.ipark_project.ui.theme.IPark_ProjectTheme
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ipark_project.buisiness.endpoints.ReservationEndpoint
import com.example.ipark_project.buisiness.entities.Parking
import com.example.ipark_project.buisiness.repositories.ReservationRepository
import com.example.ipark_project.buisiness.resources.AppDatabase
import com.example.ipark_project.buisiness.viewmodels.CreateReservationViewModel
import com.example.ipark_project.buisiness.viewmodels.MyBookingViewModel
import com.example.ipark_project.buisiness.viewmodels.ParkingsViewModel
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class MainActivity : ComponentActivity() {
    private val signUpUserViewModel: SignUpUserViewModel by viewModels {
        SignUpUserViewModel.Factory((application as MyApplication).userRepository)
    }
    private val signInUserViewModel: SignInUserViewModel by viewModels {
        SignInUserViewModel.Factory((application as MyApplication).userRepository)
    }
    private val parkingsViewModel: ParkingsViewModel by viewModels {
        ParkingsViewModel.Factory((application as MyApplication).parkingsRepository)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IPark_ProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Root
                    root()

            }
        }
    }
}
    fun clearSharedPreferences(context: Context) {
        // Get SharedPreferences instance
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        // Edit SharedPreferences to clear the stored data
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    @Composable
    fun navigationBar(navController: NavController, isConnected: MutableState<Boolean>) {
        val context = LocalContext.current
        Row(
            modifier = Modifier
                .padding(10.dp)
                .background(Color(0xFFEBEFF5))
        ) {
            BottomAppBar(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(20.dp))
                    .height(70.dp),
                backgroundColor = Color.White,
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val selectedColor = Color(0xFFD52D2D)
                val defaultColor = Color.Gray

                BottomNavigationItem(
                    selected = currentRoute == Router.HomeScreen.route,
                    onClick = { navController.navigate(Router.HomeScreen.route) },
                    icon = {
                        println(currentRoute)
                        println(Router.HomeScreen.route)
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "",
                            tint = if (currentRoute == Router.HomeScreen.route) selectedColor else defaultColor
                        )
                    }
                )
                BottomNavigationItem(
                    selected = currentRoute == Router.ReservationsScreen.route,
                    onClick = { navController.navigate(Router.ReservationsScreen.route) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = "",
                            tint = if (currentRoute == Router.ReservationsScreen.route) selectedColor else defaultColor
                        )
                    }
                )
                BottomNavigationItem(
                    selected = currentRoute == Router.ProfileScreen.route,
                    onClick = {
                        isConnected.value = false
                        clearSharedPreferences(context)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.ExitToApp,
                            contentDescription = "",
                            tint = if (currentRoute == Router.ProfileScreen.route) selectedColor else defaultColor
                        )
                    }
                )
            }
        }
    }

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun root() {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences(
        "user_prefs", Context.MODE_PRIVATE
    )
    var isConnected = remember {
        mutableStateOf<Boolean>(prefs.getBoolean("isConnected", false))
    }
    val navController = rememberNavController()

    val token = remember {
        mutableStateOf(prefs.getString("token", "") ?: "")
    }

    // Creation of reservation instances
    val database  = AppDatabase.getInstance(context)
    val reservationDao = database.getReservationDao()
    val reservationEndpoint = ReservationEndpoint.create(token.value)
    val reservationRepository = ReservationRepository(reservationEndpoint, reservationDao)
    val createReservationViewModel: CreateReservationViewModel by viewModels {
        CreateReservationViewModel.Factory(reservationRepository)
    }
    val myBookingViewModel: MyBookingViewModel by viewModels {
        MyBookingViewModel.Factory(reservationRepository)
    }

    Scaffold(
        content = { paddingValues ->
            Box(modifier = Modifier
                .padding(paddingValues)
                .background(Color(0xFFE5E5E5))) {
                NavHost(
                    navController = navController,
                    startDestination = if (isConnected.value) Router.HomeScreen.route else Router.LandingScreen.route
                ) {
                    composable(route = Router.AuthScreen.route) {
                        SignIn(
                            navController = navController, signInUserViewModel = signInUserViewModel,
                            isConnected =isConnected)
                    }
                    composable(route = Router.RegisterScreen.route) {
                        SignUp(navController = navController, signUpUserViewModel = signUpUserViewModel)
                    }
                    composable(route = Router.LandingScreen.route) {
                        Landing(navController = navController)
                    }
                    composable(route = Router.WelcomeScreen.route) {
                        Welcome(navController = navController)
                    }
                    composable(route = Router.HomeScreen.route) {
                        HomePage(navController = navController,parkingsViewModel=parkingsViewModel)
                    }
                    composable(route = Router.ReservationsScreen.route) {
                        myBookings(myBookingViewModel)
                    }
                    composable(route = Router.ParkingScreen.route) {
                           val parking= navController.previousBackStackEntry?.savedStateHandle?.get<Parking>("parking")
                                parkingDetails(
                                    parking = parking,
                                    navController = navController,
                                    createReservationViewModel = createReservationViewModel
                                )


                }
            }
        }},
        bottomBar = {
            if (isConnected.value) {
                Row (modifier = Modifier.background(Color(0xFFEBEFF5))) {
                    navigationBar(navController = navController, isConnected = isConnected)

                }
            }
        },
    )
}}