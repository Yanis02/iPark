package com.example.ipark_project.presentation

import android.net.Uri

sealed class Router(val route: String) {
    object LandingScreen: Router("/landing")
    object WelcomeScreen: Router("/welcome")
    object AuthScreen: Router("/auth")
    object RegisterScreen: Router("/register")
    object HomeScreen: Router("/home")
    object ReservationsScreen: Router("/bookings")
    object NotificationsScreen: Router("/notifs")
    object ProfileScreen: Router("/profile")
    //object ParkingScreen: Router("/parking")
    object ParkingScreen : Router("/parkingDetails")

}