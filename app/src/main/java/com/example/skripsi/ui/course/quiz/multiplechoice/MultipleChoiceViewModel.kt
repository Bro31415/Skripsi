package com.example.skripsi.ui.course.quiz.multiplechoice

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.skripsi.data.model.Question

class MultipleChoiceViewModel(
    private val question: Question,
    private val initialAnswer: String?,
    private val onAnswerSelected: (String) -> Unit
) : ViewModel() {

    // soal & opsi
    val questionText: String = question.questionText
    val options: List<String> = question.options.orEmpty()
    private val correctAnswer: String = question.answer

    // state jawaban
    var selectedAnswer by mutableStateOf<String?>(initialAnswer)
        private set

    var isAnswerCorrect by mutableStateOf<Boolean?>(initialAnswer?.let { it == correctAnswer })
        private set

    // dipanggil saat user pilih opsi
    fun selectAnswer(answer: String) {
//        if (selectedAnswer == null) {
            selectedAnswer = answer
            isAnswerCorrect = answer == correctAnswer
            onAnswerSelected(answer)

    }
}
