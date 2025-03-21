package com.example.skripsi.viewmodel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.skripsi.data.repository.UserRepository
import com.example.skripsi.utils.isEmailValid
import kotlinx.coroutines.launch

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _resetPasswordResult = MutableLiveData<Result<Unit>>()
    val resetPasswordResult: LiveData<Result<Unit>> get() = _resetPasswordResult

    fun signUp(username: String, email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = userRepository.signUpUser(email, password)
            if (success) {
                val createProfile = userRepository.createUserProfile(username)
                onResult(createProfile)
            } else {
                onResult(false)
            }
            onResult(success)
        }
    }

    fun signIn(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = userRepository.signInUser(email, password)
            onResult(success)
        }
    }

    fun createUser(username: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = userRepository.createUserProfile(username)
            onResult(success)
        }
    }

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            val result = userRepository.sendPasswordResetEmail(email)
            _resetPasswordResult.postValue(result)
        }
    }
}