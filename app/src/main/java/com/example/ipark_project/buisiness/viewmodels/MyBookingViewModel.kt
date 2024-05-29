package com.example.ipark_project.buisiness.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ipark_project.buisiness.entities.Reservation
import com.example.ipark_project.buisiness.repositories.ReservationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyBookingViewModel (private val reservationRepository: ReservationRepository): ViewModel() {
    val reservations = mutableStateOf<List<Reservation>?>(null)
    var loading = mutableStateOf(false)
    var error = mutableStateOf(false)
    var success = mutableStateOf(false)
    var errorMessage = mutableStateOf("")

    // Method to reset the error state after handling
    fun errorHandled() {
        error.value = false
    }

    fun getAllReservations () {
        loading.value = true
        viewModelScope.launch {
            val reservationsFromRepo = reservationRepository.getMyReservations()
            withContext(Dispatchers.Main) {
                reservations.value = reservationsFromRepo
                if(reservationsFromRepo.isEmpty()){
                    error.value = true
                    errorMessage.value = "No reservation exists"
                }
                else{
                    success.value = true
                }
                loading.value = false

            }
        }
    }
    class Factory(private val reservationRepository: ReservationRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MyBookingViewModel(reservationRepository) as T
        }
    }
}