package com.example.skripsi.ui.course.quiz.matchwords

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.data.model.Question
import com.example.skripsi.viewmodel.MatchViewModel

class MatchViewModelFactory (
    private val question: Question
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MatchViewModel::class.java)){
            return MatchViewModel(question) as T
        }
        throw IllegalArgumentException("Unknown Viewmodel Class")
    }
}