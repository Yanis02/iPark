package com.example.ipark_project.buisiness.endpoints

import com.example.ipark_project.buisiness.URL
import retrofit2.http.GET
import com.example.ipark_project.buisiness.entities.Parking
import com.google.android.gms.common.api.Result
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface ParkingsEndpoints{
    @GET("api/parkings/")
    suspend fun getParkings(): Response<List<Parking>>
    companion object {

        private var endpoint: ParkingsEndpoints? = null

        fun create(): ParkingsEndpoints {
            if (endpoint == null) {
                val client = OkHttpClient.Builder()
                    .followRedirects(true) // Disable redirects
                    .build()
                endpoint = Retrofit.Builder()
                    .baseUrl(URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ParkingsEndpoints::class.java)
            }
            return endpoint!!
        }
    }
}
