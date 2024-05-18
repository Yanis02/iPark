package com.example.ipark_project.buisiness.repositories

import com.example.ipark_project.buisiness.endpoints.AuthEndpoint
import com.example.ipark_project.buisiness.endpoints.SignInRequest
import com.example.ipark_project.buisiness.endpoints.SignInResponse
import com.example.ipark_project.buisiness.endpoints.SignUpRequest
import com.example.ipark_project.buisiness.entities.User
import retrofit2.Response

class UserRepository(private val authEndpoint: AuthEndpoint) {

    suspend fun signUp(
        username: String, email: String, password: String, gender: String, location: String
    ): Response<User> {
        val signUpRequest = SignUpRequest(username, email, password, gender, location)
        println("Signing up with request: $signUpRequest")
        return authEndpoint.signUp(signUpRequest)
    }

    suspend fun signIn(
        username: String,
        password: String
    ): Response<SignInResponse> {
        val signInRequest = SignInRequest(username, password)
        println("Signing in with request: $signInRequest")
        return authEndpoint.signIn(signInRequest)
    }
}
