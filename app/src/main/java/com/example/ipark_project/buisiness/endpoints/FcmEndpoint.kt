package com.example.ipark_project.buisiness.endpoints

import com.example.ipark_project.buisiness.URL
import com.example.ipark_project.buisiness.entities.User
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class FcmRequest(
    val token: String
)
interface FcmEndpoint  {

    @POST("api/users/update_fcm_token/")
    suspend fun updateFcm(
        @Body fcmRequest:FcmRequest
    ): Response<String>


    companion object {

        private var endpoint: FcmEndpoint? = null

        fun create(): FcmEndpoint {
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
