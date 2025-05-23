package com.example.skripsi.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skripsi.MyApp.Companion.supabase
import com.example.skripsi.data.model.Question
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch

class QuizViewModel(private val quizId: String) : ViewModel() {

    private val _questions = mutableStateListOf<Question>()
    val questions: List<Question> get() = _questions

    var questionIndex by mutableIntStateOf(0)
        private set

    val currentQuestion: Question?
        get() = _questions.getOrNull(questionIndex)

    var totalXpEarned by mutableIntStateOf(0)
        private set

    val isQuizFinished: Boolean
        get() = questionIndex >= _questions.size

    fun setQuestions(questionList: List<Question>) {
        _questions.clear()
        _questions.addAll(questionList)
    }

    fun submitAnswer(isCorrect: Boolean) {
        currentQuestion?.let {
            if (isCorrect) totalXpEarned += it.xp?.toInt() ?: 0
        }
        questionIndex++
    }
}