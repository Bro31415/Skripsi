package com.example.skripsi.viewmodel

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.skripsi.data.repository.UserRepository
import com.example.skripsi.utils.isEmailValid
import kotlinx.coroutines.launch

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun signUp(username: String, email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = userRepository.signUpUser(email, password)
            onResult(success)
        }
    }

    fun signIn(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = userRepository.signInUser(email, password)
            onResult(success)
        }
    }
}