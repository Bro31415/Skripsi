package com.example.skripsi.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skripsi.data.model.ChapterWithQuizzes
import com.example.skripsi.data.repository.CourseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CourseViewModel (private val courseRepository: CourseRepository) : ViewModel() {

    private val _chaptersWithQuizzes = MutableStateFlow<List<ChapterWithQuizzes>>(emptyList())
    val chapterWithQuizzes: StateFlow<List<ChapterWithQuizzes>> = _chaptersWithQuizzes

    init {
        fetchChapters()
    }

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