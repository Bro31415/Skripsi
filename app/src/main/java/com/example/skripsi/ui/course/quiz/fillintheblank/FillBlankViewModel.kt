package com.example.skripsi.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.skripsi.data.model.Question

@Immutable
data class FillInTheBlankUiState(
    val mainQuestion: String,
    val sentenceParts: List<String>,
    val options: List<String>,
    val correctAnswer: String, // Added to hold the correct answer
    val selectedAnswer: String? = null,
    val isSubmitted: Boolean = false,
    val isCorrect: Boolean? = null
)

class FillInTheBlankViewModel(val question: Question) : ViewModel() {

    var uiState by mutableStateOf(createInitialState())
        private set

    private fun createInitialState(): FillInTheBlankUiState {
        val questionSegments = question.questionText.split('|')
        val mainQuestionText = questionSegments.getOrNull(0)?.trim() ?: ""
        val sentenceWithBlank = questionSegments.getOrNull(1)?.trim() ?: "_____"

        return FillInTheBlankUiState(
            mainQuestion = mainQuestionText,
            sentenceParts = sentenceWithBlank.split("_____"),
            options = question.options.orEmpty(),
            correctAnswer = question.answer
        )
    }

    fun onAnswerSelected(option: String) {
        if (!uiState.isSubmitted) {
            uiState = uiState.copy(selectedAnswer = option)
        }
    }

    fun onSubmit() {
        if (uiState.selectedAnswer == null) return

        val isAnswerCorrect = uiState.selectedAnswer == question.answer

        uiState = uiState.copy(
            isSubmitted = true,
            isCorrect = isAnswerCorrect
        )
    }
}
