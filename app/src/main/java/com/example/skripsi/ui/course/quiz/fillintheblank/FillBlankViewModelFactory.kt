package com.example.skripsi.ui.course.quiz.fillintheblank

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.data.model.Question
import com.example.skripsi.viewmodel.FillInTheBlankViewModel

class FillInTheBlankViewModelFactory(
    private val question: Question
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FillInTheBlankViewModel::class.java)) {
            return FillInTheBlankViewModel(question) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
