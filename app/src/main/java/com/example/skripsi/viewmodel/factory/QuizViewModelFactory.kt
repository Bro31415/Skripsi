package com.example.skripsi.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.viewmodel.QuizViewModel

class QuizViewModelFactory (private val quizId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            return QuizViewModel(quizId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}