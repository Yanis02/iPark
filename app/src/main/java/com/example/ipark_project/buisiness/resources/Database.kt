package com.example.ipark_project.buisiness.resources

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ipark_project.buisiness.dao.ReservationDao
import com.example.ipark_project.buisiness.entities.Reservation

@Database(entities = [Reservation::class], version = 1)
abstract class AppDatabase : RoomDatabase(

) {
    abstract fun getReservationDao(): ReservationDao
    companion object {
        var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(
                        context, AppDatabase::class.java,
                        "app_db",
                    ).build()
            }
            return instance!!
        }
    }
}