package com.example.skripsi.ui.course.quiz.multiplechoice

import MultipleChoiceViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.data.model.Question

class MultipleChoiceViewModelFactory(
    private val question: Question
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MultipleChoiceViewModel::class.java)) {
            return MultipleChoiceViewModel(question) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
