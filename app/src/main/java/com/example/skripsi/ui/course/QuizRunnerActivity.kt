package com.example.skripsi.ui.course

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

        quizViewModel = ViewModelProvider(
            this,
            QuizViewModelFactory(quizId)
        )[QuizViewModel::class.java]

        lifecycleScope.launch {
            val repo = CourseRepository(MyApp.supabase)
            val questions = repo.getQuestionsByQuizId(quizIdLong)

            if (questions.isEmpty()) {
                Log.e("QuizRunnerActivity", "No questions found for quizId $quizId")
                finish()
                return@launch
            }

            quizViewModel.setQuestions(questions)
            showQuestion()
        }
    }

    private fun showQuestion() {
        val question = quizViewModel.currentQuestion ?: run {
            Log.i("QuizRunnerActivity", "Quiz Finished. XP Earned: ${quizViewModel.totalXpEarned}")
            finish()
            return
        }

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

    fun onAnswerSelected(questionId: Long, selectedAnswer: String) {
        answers[questionId] = selectedAnswer
    }

    fun submitAnswer(isCorrect: Boolean) {
        quizViewModel.submitAnswer(isCorrect)
        Handler(Looper.getMainLooper()).postDelayed({
            showQuestion()
        }, 5000)
    }
}
