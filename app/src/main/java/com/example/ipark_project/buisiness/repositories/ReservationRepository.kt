package com.example.ipark_project.buisiness.repositories

import com.example.ipark_project.buisiness.dao.ReservationDao
import com.example.ipark_project.buisiness.endpoints.CreateReservationRequest
import com.example.ipark_project.buisiness.endpoints.ParkingsEndpoints
import com.example.ipark_project.buisiness.endpoints.ReservationEndpoint
import com.example.ipark_project.buisiness.entities.Parking
import com.example.ipark_project.buisiness.entities.Reservation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class ReservationRepository (
    private val reservationEndpoint: ReservationEndpoint,
    private val reservationDao: ReservationDao
    ) {
    suspend fun createReservation(
        parking : String,
        entry_date: String,
        entry_time: String,
        exit_date: String,
        exit_time: String
    ): Response<Reservation> {
        val createReservationRequest = CreateReservationRequest(
            parking, entry_date, entry_time, exit_date, exit_time
        )
        return reservationEndpoint.createReservation(createReservationRequest)
    }
    fun createLocalReservation(reservation: Reservation) =
        reservationDao.createReservation(reservation = reservation)
    fun deleteLocalReservations() =
        reservationDao.deleteFromReservations()
    suspend fun getMyReservations(): List<Reservation> {
        return withContext(Dispatchers.IO) {
            reservationDao.getReservations()
        }
    }
}