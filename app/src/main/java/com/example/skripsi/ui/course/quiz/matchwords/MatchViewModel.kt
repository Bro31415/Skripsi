package com.example.skripsi.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import com.example.skripsi.data.model.Question

@Immutable
data class MatchUiState(
    val questionText: String,
    val wordBankOptions: List<String>,
    val selectedAnswerWords: List<String> = emptyList(),
    val isSubmitted: Boolean = false,
    val isCorrect: Boolean? = null
)

class MatchViewModel(val question: Question) : ViewModel() {

    var uiState by mutableStateOf(createInitialState())
        private set

    private fun createInitialState(): MatchUiState {
        return MatchUiState(
            questionText = question.questionText,
            wordBankOptions = question.options.orEmpty().shuffled()
        )
    }


    fun onWordBankChipClicked(word: String) {
        if (uiState.isSubmitted) return

        val newSelectedWords = uiState.selectedAnswerWords + word
        val newWordBank = uiState.wordBankOptions - word
        uiState = uiState.copy(
            selectedAnswerWords = newSelectedWords,
            wordBankOptions = newWordBank
        )
    }

    fun onAnswerChipClicked(word: String) {
        if (uiState.isSubmitted) return

        val newSelectedWords = uiState.selectedAnswerWords - word
        val newWordBank = uiState.wordBankOptions + word
        uiState = uiState.copy(
            selectedAnswerWords = newSelectedWords,
            wordBankOptions = newWordBank
        )
    }

    fun onSubmit() {
        if (uiState.selectedAnswerWords.isEmpty()) return
        val userAnswer = uiState.selectedAnswerWords.joinToString(" ")
        val isAnswerCorrect = userAnswer.equals(question.answer, ignoreCase = true)

        uiState = uiState.copy(
            isSubmitted = true,
            isCorrect = isAnswerCorrect
        )
    }
}