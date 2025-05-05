package com.example.skripsi.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MatchViewModel : ViewModel() {

    private val _wordList = listOf("kumaha", "damang", "?") // ambil dari db
    val wordList: List<String> get() = _wordList


    var selectedWords by mutableStateOf(listOf<String>())
        private set

    val correctSentence = "kumaha damang?" // ambil dari db

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
