package com.example.skripsi.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.data.repository.CourseRepository
import com.example.skripsi.viewmodel.QuizViewModel

class QuizViewModelFactory (private val quizId: String, private val repository: CourseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            return QuizViewModel(quizId, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}