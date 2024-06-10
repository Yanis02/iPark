package com.example.ipark_project.buisiness.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.ipark_project.buisiness.entities.Reservation

@Dao
interface ReservationDao {

    @Insert()
    fun createReservation(reservation: Reservation)

    @Query("SELECT * FROM RESERVATIONS")
    fun getReservations():List<Reservation>

    @Query("DELETE FROM RESERVATIONS")
    fun deleteFromReservations()

}