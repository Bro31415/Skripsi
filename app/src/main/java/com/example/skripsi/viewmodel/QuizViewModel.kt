package com.example.skripsi.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import co.touchlab.kermit.Message
import com.example.skripsi.MyApp
import com.example.skripsi.MyApp.Companion.supabase
import com.example.skripsi.data.model.Question
import com.example.skripsi.data.model.Quiz
import com.example.skripsi.data.model.UserQuizAttempt
import com.example.skripsi.data.repository.CourseRepository
import com.example.skripsi.data.repository.UserProgressRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import java.time.Instant

sealed class QuizUiState {
    object Loading : QuizUiState()
    data class ShowQuestion(val question: Question, val index: Int, val total: Int) : QuizUiState()
    data class ShowFeedback(val isCorrect: Boolean) : QuizUiState()
    data class Finished(val totalXp: Int) : QuizUiState()
    data class Error(val message: String) : QuizUiState()
}

class QuizViewModel(
    private val quizId: String,
    private val courseRepository: CourseRepository,
    private val userProgressRepository: UserProgressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<QuizUiState>(QuizUiState.Loading)
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private var questions = emptyList<Question>()
    private var questionIndex = 0
    private var totalXp = 0
    private var totalQuestions = 0
    private var quizStartTime: Long = 0
    private var quizEndTime: Long = 0

    init {
        fetchQuestions()
    }

    fun startQuizTimer() {
        quizStartTime = System.currentTimeMillis()
    }

    fun endQuizTimer() {
        quizEndTime = System.currentTimeMillis()
    }

    fun getQuizDurationInSeconds(): Int {
        return ((quizEndTime - quizStartTime) / 1000).toInt()
    }

    private fun fetchQuestions() {
        viewModelScope.launch {
            try {
                val quizIdLong = quizId.toLong()
                questions = courseRepository.getQuestionsByQuizId(quizIdLong)
                totalQuestions = questions.size
                if (questions.isEmpty()) {
                    _uiState.value = QuizUiState.Error("No questions found")
                } else {
                    _uiState.value = QuizUiState.ShowQuestion(
                        question = questions[questionIndex],
                        index = questionIndex + 1,
                        total = questions.size
                    )
                    quizStartTime = System.currentTimeMillis()
                }
            } catch (e: Exception) {
                _uiState.value = QuizUiState.Error("Failed to load quiz: ${e.message}")
            }
        }
    }

    fun handleAnswer(isCorrect: Boolean) {
        viewModelScope.launch {
            if (isCorrect) {
                totalXp += (questions[questionIndex].xp?.toInt() ?: 0)
            }

            _uiState.value = QuizUiState.ShowFeedback(isCorrect)
            delay(1000L)

            questionIndex++

            if (questionIndex >= questions.size) {
                _uiState.value = QuizUiState.Finished(totalXp)
            } else {
                _uiState.value = QuizUiState.ShowQuestion(
                    question = questions[questionIndex],
                    index = questionIndex + 1,
                    total = questions.size
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun logQuizResultBlocking(totalXp: Int) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: return
        quizEndTime = System.currentTimeMillis()

        val attempt = UserQuizAttempt(
            userId = userId,
            quizId = quizId.toLong(),
            totalQuestions = totalQuestions,
            xpEarned = totalXp,
                    quizDuration = getQuizDurationInSeconds(),
                    startedAt = Instant.ofEpochMilli(quizStartTime).toString(),
                    finishedAt = Instant.ofEpochMilli(quizEndTime).toString()
                )

                val inserted = userProgressRepository.insertUserQuizAttempt(attempt)
                if (!inserted) {
                    Log.e("QuizViewModel", "Quiz attempt insert failed")
                }

                val updated = userProgressRepository.updateUserXP(userId, totalXp)
                if (!updated) {
                    Log.e("QuizViewModel", "XP update failed")
                }

    }
}