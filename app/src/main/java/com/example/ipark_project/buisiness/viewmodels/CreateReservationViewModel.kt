package com.example.ipark_project.buisiness.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ipark_project.buisiness.entities.Reservation
import com.example.ipark_project.buisiness.repositories.ReservationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ConnectException

class CreateReservationViewModel (private val reservationRepository: ReservationRepository): ViewModel(){
    val reservation = mutableStateOf<Reservation?>(null)
    var loading = mutableStateOf(false)
    var error = mutableStateOf(false)
    var connectionError = mutableStateOf(false)
    var success = mutableStateOf(false)
    var errorMessage = mutableStateOf("")

    fun CreateReservation (
        parking : String,
        entry_date: String,
        entry_time: String,
        exit_date: String,
        exit_time: String,
        context: Context
    ) {
        loading.value = true
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val prefs = context.getSharedPreferences(
                        "user_prefs", Context.MODE_PRIVATE
                    )
                    val token = prefs.getString("token", "") ?: ""
                    reservationRepository.updateEndpoint(token)
                    val response = reservationRepository.createReservation(
                        parking = parking,
                        entry_date = entry_date,
                        entry_time = entry_time,
                        exit_date = exit_date,
                        exit_time = exit_time
                    )
                    if (response.isSuccessful) {
                        val data = response.body()
                        Log.v("codee", data.toString())
                        if (data != null) {
                            reservation.value = data
                            //reservationRepository.deleteLocalReservations()
                            reservationRepository.createLocalReservation(data)
                            success.value = true
                        }
                    } else {
                        if (response.code().toString() == "409"){
                            errorMessage.value = "Unavailable places for the request"
                        }else{
                            errorMessage.value = "an error occured when creating the reservation"
                        }
                        error.value = true
                    }
                    loading.value = false
                }
            }catch (e: ConnectException){
                connectionError.value = true
            }finally {
                loading.value = false
            }
        }
    }
    class Factory(private val reservationRepository: ReservationRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CreateReservationViewModel(reservationRepository) as T
        }
    }
}
