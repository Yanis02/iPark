package com.example.ipark_project.buisiness.repositories

import com.example.ipark_project.buisiness.endpoints.ParkingsEndpoints
import com.example.ipark_project.buisiness.entities.Parking
import retrofit2.Response

class ParkingsRepository (private val parkingsEndpoins: ParkingsEndpoints) {
    suspend fun getParkings(): Response<List<Parking>>{
        return parkingsEndpoins.getParkings()
    }
}