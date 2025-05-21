package com.example.skripsi.ui.course.quiz.multiplechoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.data.model.Question

class MultipleChoiceViewModelFactory(
    private val question: Question,
    private val initialAnswer: String?,
    private val onAnswerSelected: (String) -> Unit
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(c: Class<T>): T {
        if (c.isAssignableFrom(MultipleChoiceViewModel::class.java)) {
            return MultipleChoiceViewModel(question, initialAnswer, onAnswerSelected) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
