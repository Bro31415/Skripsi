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

    val currentUserId: String = ""

    init {
        loadLeaderboard()
    }

    private fun loadLeaderboard() {
        viewModelScope.launch {
            try {
                _topUsers.value = userRepository.getTopUsers(limit = 15)
            } catch (e: Exception) {
                _topUsers.value = emptyList()
            }
        }
    }
}