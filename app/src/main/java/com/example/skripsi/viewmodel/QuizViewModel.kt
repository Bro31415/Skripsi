package com.example.skripsi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skripsi.data.model.Question
import com.example.skripsi.data.repository.QuizRepository
import kotlinx.coroutines.launch

class QuizViewModel (private val quizRepository: QuizRepository) : ViewModel() {

    private val _quizId = MutableLiveData<Long>()
    val quizId: LiveData<Long> get() = _quizId

    private val _questions = MutableLiveData<List<Question>>()
    val question: LiveData<List<Question>> get() = _questions

    fun fetchQuizId(chapterId: Long, quizType: String) {
        viewModelScope.launch {
            _quizId.value = quizRepository.getQuizId(chapterId, quizType)
        }
    }

    fun fetchQuizQuestions(quizId: Long) {
        viewModelScope.launch {
            _questions.value = quizRepository.getQuizQuestions(quizId)
        }
    }
}