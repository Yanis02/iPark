package com.example.ipark_project.presentation

sealed class Router(val route: String) {
    object LandingScreen: Router("/landing")
    object WelcomeScreen: Router("/welcome")
    object AuthScreen: Router("/auth")
    object RegisterScreen: Router("/register")

}