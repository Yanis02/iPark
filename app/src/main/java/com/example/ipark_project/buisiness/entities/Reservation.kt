package com.example.ipark_project.buisiness.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservations")
data class Reservation(
    @PrimaryKey
    val id: String,
    val parking: String,
    val entry_date: String,
    val entry_time: String,
    val exit_date: String,
    val exit_time: String,
    val qrcode: String
)
