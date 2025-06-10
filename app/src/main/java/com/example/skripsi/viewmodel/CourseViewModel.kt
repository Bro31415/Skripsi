package com.example.skripsi.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skripsi.data.model.ChapterWithQuizzes
import com.example.skripsi.data.repository.CourseRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CourseViewModel (private val courseRepository: CourseRepository, private val supabase: SupabaseClient) : ViewModel() {

    private val _chaptersWithQuizzes = MutableStateFlow<List<ChapterWithQuizzes>>(emptyList())
    val chapterWithQuizzes: StateFlow<List<ChapterWithQuizzes>> = _chaptersWithQuizzes

    init {
        fetchChapters()
    }

    val username: StateFlow<String> = flow {

        val userId = supabase.auth.currentUserOrNull()?.id

        if (userId != null) {
            val userProfile = courseRepository.getUserProfile(userId)
            emit(userProfile?.username ?: "User")
        } else {
            emit("Guest")
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "User")

    private fun fetchChapters() {
        viewModelScope.launch {
            try {
                Log.d("CourseViewModel", "Fetching chapters")
                val result = courseRepository.getChaptersWithQuizzes()
                Log.d("CourseViewModel", "Loaded chapters: ${result.size}")
                _chaptersWithQuizzes.value = result
            } catch (e: Exception) {
                Log.e("CourseViewModel", "Failed to load chapters", e)
            }
        }
    }


}