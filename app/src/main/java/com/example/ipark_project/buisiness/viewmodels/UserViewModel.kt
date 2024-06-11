package com.example.ipark_project.buisiness.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ipark_project.buisiness.endpoints.SignInResponse
import com.example.ipark_project.buisiness.entities.User
import com.example.ipark_project.buisiness.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ConnectException

class SignUpUserViewModel (private val userRepository: UserRepository) : ViewModel() {
    val user = mutableStateOf<User?>(null)
    var loading = mutableStateOf(false)
    var error = mutableStateOf(false)
    var success = mutableStateOf(false)
    var connectionError = mutableStateOf(false)
    fun signUpUser(
        username: String, email: String, password: String, gender: String, location: String
    ){
        loading.value = true
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val response =
                        userRepository.signUp(username, email, password, gender, location)
                    if (response.isSuccessful) {
                        val data = response.body()
                        Log.v("codee", data.toString())
                        if (data != null) {
                            user.value = data
                            success.value = true
                        }
                    } else {
                        Log.v("codee", response.body().toString())
                        error.value = true
                    }
                    loading.value = false
                }
            }catch(e: ConnectException){
                connectionError.value = true
            }finally {
                loading.value = false
            }
        }
    }

    class Factory(private val userRepository: UserRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SignUpUserViewModel(userRepository) as T
        }
    }

}

class SignInUserViewModel(private val userRepository: UserRepository) : ViewModel() {
    val signInResponse = mutableStateOf<SignInResponse?>(null)
    var loading = mutableStateOf(false)
    var error = mutableStateOf(false)
    var success = mutableStateOf(false)
    var connectionError = mutableStateOf(false)

    fun signInUser(username: String, password: String) {
        loading.value = true
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val response = userRepository.signIn(username = username, password = password)
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data != null) {
                            signInResponse.value = data
                            // Save token to SharedPreferences
                            success.value = true
                        }
                    } else {
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

    class Factory(private val userRepository: UserRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SignInUserViewModel(userRepository) as T
        }
    }
}