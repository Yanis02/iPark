package com.example.ipark_project

import android.app.Application
import com.example.ipark_project.buisiness.endpoints.AuthEndpoint
import com.example.ipark_project.buisiness.repositories.UserRepository

class MyApplication : Application(){
    private val endpoint by lazy { AuthEndpoint.create() }
    val userRepository by lazy { UserRepository(endpoint) }
}