package com.example.ipark_project.buisiness.endpoints

import com.example.ipark_project.buisiness.URL
import com.example.ipark_project.buisiness.entities.User
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body

data class SignUpRequest(
    val username: String,
    val email: String,
    val password: String,
    val gender: String,
    val location: String
)

data class SignInRequest(
    val username: String,
    val password: String,
)

data class SignInResponse(
    val token: String
)


interface AuthEndpoint  {

    @POST("api/users/signup/")
    suspend fun signUp(
        @Body signUpRequest: SignUpRequest
    ): Response<User>

    @POST("api/users/signin/")
    suspend fun signIn(
        @Body signInRequest: SignInRequest
    ): Response<SignInResponse>

    companion object {

        private var endpoint: AuthEndpoint? = null

        fun create(): AuthEndpoint {
            if (endpoint == null) {
                val client = OkHttpClient.Builder()
                    .followRedirects(true) // Disable redirects
                    .build()
                endpoint = Retrofit.Builder()
                    .baseUrl(URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(AuthEndpoint::class.java)
            }
            return endpoint!!
        }
    }
}
