package com.example.skripsi.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import com.example.skripsi.data.model.Question

class MatchViewModel(private val question: Question) : ViewModel() {

    private val _wordList = question.options
    val wordList: List<String>? get() = _wordList
    val questionText: String = question.questionText

    var selectedWords by mutableStateOf(listOf<String>())
        private set

    private val correctSentence = question.answer

    var isAnswerCorrect by mutableStateOf<Boolean?>(null)
        private set

    fun selectWord(word: String) {
        if (word !in selectedWords) {
            selectedWords = selectedWords + word
            validateAnswer()
        }
    }

    fun removeWord(word: String) {
        selectedWords = selectedWords - word
        isAnswerCorrect = null
    }

    private fun validateAnswer() {
        val userSentence = selectedWords.joinToString(" ").replace(" ?", "?")
        isAnswerCorrect = userSentence == correctSentence
    }

    fun reset() {
        selectedWords = emptyList()
        isAnswerCorrect = null
    }
}
