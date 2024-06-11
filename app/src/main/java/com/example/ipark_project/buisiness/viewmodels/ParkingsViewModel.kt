package com.example.ipark_project.buisiness.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ipark_project.buisiness.entities.Parking
import com.example.ipark_project.buisiness.repositories.ParkingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ConnectException

class ParkingsViewModel (private val parkingsRepository: ParkingsRepository):ViewModel(){
val parkings= mutableStateOf<List<Parking>?>(null)
    var loading = mutableStateOf(false)
    var error = mutableStateOf(false)
    var success = mutableStateOf(false)
    var connectionError = mutableStateOf(false)
    fun getParkings () {
        loading.value = true
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val response = parkingsRepository.getParkings()
                    if (response.isSuccessful) {
                        val data = response.body()
                        Log.v("codee", data.toString())
                        if (data != null) {
                            parkings.value = data
                            success.value = true
                        }
                    } else {
                        Log.v("codee", response.body().toString())
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
    class Factory(private val parkingsRepository: ParkingsRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ParkingsViewModel(parkingsRepository) as T
        }
    }
}