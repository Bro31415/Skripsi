package com.example.skripsi.ui.course.quiz.fillintheblank

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.data.model.Question
import com.example.skripsi.viewmodel.FillBlankViewModel

class FillBlankViewModelFactory(
    private val question: Question
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FillBlankViewModel::class.java)) {
            return FillBlankViewModel(question) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
