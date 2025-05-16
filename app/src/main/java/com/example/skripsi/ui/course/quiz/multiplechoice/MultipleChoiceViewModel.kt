package com.example.skripsi.ui.quiz.multiplechoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skripsi.data.model.Question
import com.example.skripsi.data.repository.QuestionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MultipleChoiceViewModel : ViewModel() {

    private val repository = QuestionRepository()

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions

    fun loadQuestions(quizId: Int) {
        viewModelScope.launch {
            val result = repository.getMultipleChoiceQuestions(quizId)
            _questions.value = result
        }
    }
}
