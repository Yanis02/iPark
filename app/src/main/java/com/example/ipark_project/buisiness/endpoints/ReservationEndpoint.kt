package com.example.ipark_project.buisiness.endpoints

import com.example.ipark_project.buisiness.URL
import com.example.ipark_project.buisiness.entities.Reservation
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class CreateReservationRequest(
    val parking : String,
    val entry_date: String,
    val entry_time: String,
    val exit_date: String,
    val exit_time: String
)

interface ReservationEndpoint {

    @POST("api/reservations/create/")
    suspend fun createReservation(
        @Body createReservationRequest: CreateReservationRequest
    ): Response<Reservation>

    companion object {

        fun create(token: String): ReservationEndpoint {
            val client = OkHttpClient.Builder()
                .followRedirects(true)
                .addInterceptor(AuthInterceptor(token))
                .build()
            val endpoint = Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ReservationEndpoint::class.java)
            return endpoint!!
        }
    }
}
