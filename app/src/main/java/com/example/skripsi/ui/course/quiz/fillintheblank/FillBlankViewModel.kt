package com.example.skripsi.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.skripsi.data.model.Question

class FillBlankViewModel(val question: Question) : ViewModel() {

    var selectedAnswer by mutableStateOf<String?>(null)
        private set

    var isAnswerCorrect by mutableStateOf<Boolean?>(null)
        private set

    fun selectAnswer(answer: String) {
        selectedAnswer = answer
        isAnswerCorrect = answer == question.answer
    }

    fun reset() {
        selectedAnswer = null
        isAnswerCorrect = null
    }
}
