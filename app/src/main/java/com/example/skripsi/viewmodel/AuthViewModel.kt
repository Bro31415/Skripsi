package com.example.skripsi.viewmodel

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.skripsi.MyApp
import com.example.skripsi.data.model.User
import com.example.skripsi.data.repository.UserRepository
import com.example.skripsi.utils.isEmailValid
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

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

    //tambahan wil
    fun getUserProfile(userId: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            val user = userRepository.getUserProfile(userId)
            onResult(user)
        }
    }

    //tambahan updateUsername
    fun updateUsername(newUsername: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val currentUser = MyApp.supabase.auth.currentUserOrNull()
            val userId = currentUser?.id

            if (userId != null) {
                val success = userRepository.updateUsername(userId, newUsername)
                onResult(success)
            } else {
                onResult(false)
            }
        }
    }
}