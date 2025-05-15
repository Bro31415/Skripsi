package com.example.skripsi.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class FillBlankViewModel : ViewModel() {

    // Data class for questions
    data class Question(
        val questionText: String,
        val translation: String,
        val options: List<String>,
        val correctAnswerIndex: Int
    )

    // Sample questions list
    private val questions = listOf(
        Question(
            questionText = "Kumaha __________ ?",
            translation = "Bagaimana, sehat?",
            options = listOf("damang", "nami abdi", "abdi", "abdi sorangan"),
            correctAnswerIndex = 0
        ),
        // Add more questions here
    )

    // Current question index
    var currentQuestionIndex by mutableStateOf(0)
        private set

    // Total questions count
    val totalQuestions = questions.size

    // Current selected option (-1 means nothing selected)
    var selectedOption by mutableStateOf(-1)
        private set

    // Get current question based on index
    val currentQuestion: Question
        get() = questions[currentQuestionIndex]

    // User feedback state
    var feedbackState by mutableStateOf<FeedbackState?>(null)
        private set

    // Possible feedback states
    sealed class FeedbackState {
        object Correct : FeedbackState()
        object Incorrect : FeedbackState()
    }

    // Function to select an option
    fun selectOption(index: Int) {
        selectedOption = index
        checkAnswer()
    }

    // Function to check if the answer is correct
    private fun checkAnswer() {
        if (selectedOption == currentQuestion.correctAnswerIndex) {
            feedbackState = FeedbackState.Correct
            // Move to next question after a delay
            moveToNextQuestion()
        } else {
            feedbackState = FeedbackState.Incorrect
        }
    }

    // Function to move to the next question
    private fun moveToNextQuestion() {
        if (currentQuestionIndex < questions.size - 1) {
            // Delay could be implemented here, but requires a coroutine scope
            currentQuestionIndex++
            selectedOption = -1
            feedbackState = null
        } else {
            // Quiz completed - could handle this with a callback or state
        }
    }

    // Reset the quiz
    fun resetQuiz() {
        currentQuestionIndex = 0
        selectedOption = -1
        feedbackState = null
    }
}