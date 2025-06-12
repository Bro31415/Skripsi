package com.example.skripsi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skripsi.data.model.User
import com.example.skripsi.data.repository.UserRepository
import kotlinx.coroutines.launch

class LeaderboardViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _topUsers = MutableLiveData<List<User>>()
    val topUsers: LiveData<List<User>> = _topUsers

    // Assume you have a way to get the current user's ID
    val currentUserId: String =
        "some_current_user_id" // TODO: Replace with actual logged-in user ID

    init {
        loadLeaderboard()
    }

    private fun loadLeaderboard() {
        viewModelScope.launch {
            try {
                // Fetch top 15 users as per your design
                _topUsers.value = userRepository.getTopUsers(limit = 15)
            } catch (e: Exception) {
                // Handle error
                _topUsers.value = emptyList()
            }
        }
    }
}

