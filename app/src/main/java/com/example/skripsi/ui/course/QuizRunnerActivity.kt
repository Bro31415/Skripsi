package com.example.skripsi.ui.course

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.skripsi.MyApp
import com.example.skripsi.R
import com.example.skripsi.data.model.Question
import com.example.skripsi.data.repository.CourseRepository
import com.example.skripsi.ui.course.quiz.multiplechoice.MultipleChoiceFragment
import com.example.skripsi.ui.course.quiz.state.ErrorFragment
import com.example.skripsi.ui.course.quiz.state.LoadingFragment
import com.example.skripsi.viewmodel.QuizUiState
import com.example.skripsi.viewmodel.QuizViewModel
import com.example.skripsi.viewmodel.factory.QuizViewModelFactory
import kotlinx.coroutines.launch

class QuizRunnerActivity : AppCompatActivity() {

        private lateinit var quizViewModel: QuizViewModel
        private lateinit var quizId: String
        private val answers = mutableMapOf<Long, String>()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(R.layout.activity_quiz_runner)

            val quizIdLong = intent.getLongExtra("quizId", -1L)
            if (quizIdLong == -1L) {
                Log.e("QuizRunnerActivity", "No quizId received")
                finish()
                return
            }
            quizId = quizIdLong.toString()

            val repository = CourseRepository(MyApp.supabase)

            quizViewModel = ViewModelProvider(
                this,
                QuizViewModelFactory(quizId, repository)
            )[QuizViewModel::class.java]

            lifecycleScope.launch {
                quizViewModel.uiState.collect { state ->
                    when (state) {
                        is QuizUiState.Loading -> showLoading()
                        is QuizUiState.ShowQuestion -> showQuestion(state.question, state.index, state.total)
                        is QuizUiState.ShowFeedback -> showFeedbackScreen(state.isCorrect)
                        is QuizUiState.Finished -> showResultScreen(state.totalXp)
                        is QuizUiState.Error -> showErrorScreen(state.message)
                    }
                }
            }

        }

        private fun showQuestion(question: Question, index: Int, total: Int) {
            val savedAnswer = answers[question.id]

            val fragment = when (question.questionType) {
                "match" -> MatchFragment.newInstance(question)
                "fill_in_the_blank" -> FillInTheBlankFragment() // TODO: add newInstance() method
                "multiple_choice" -> MultipleChoiceFragment.newInstance(question, savedAnswer)
                else -> {
                    Log.e("QuizRunnerActivity", "Unknown question type: ${question.questionType}")
                    return
                }
            }

            supportFragmentManager.commit {
                replace(R.id.quizFragmentContainer, fragment)
            }
        }

        private fun showLoading() {
            val fragment = LoadingFragment()
            supportFragmentManager.commit {
                replace(R.id.quizFragmentContainer, fragment)
            }
        }

        private fun showErrorScreen(message: String) {
            val fragment = ErrorFragment.newInstance(message)
            supportFragmentManager.commit {
                replace(R.id.quizFragmentContainer, fragment)
            }
        }

        private fun showFeedbackScreen(isCorrect: Boolean) {
            val fragment = QuestionFeedbackFragment.newInstance(isCorrect)
            supportFragmentManager.commit {
                replace(R.id.quizFragmentContainer, fragment)
            }
        }

        private fun showResultScreen(totalXp: Int) {
            val intent = Intent(this, QuizResultActivity::class.java)
            intent.putExtra("totalXp", totalXp)
            startActivity(intent)
            finish()
        }

        fun onAnswerSelected(questionId: Long, selectedAnswer: String) {
            answers[questionId] = selectedAnswer
        }

        fun submitAnswer(isCorrect: Boolean) {
            quizViewModel.handleAnswer(isCorrect)
        }

        fun retryFetchQuestions() {

        }
}
