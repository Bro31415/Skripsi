package com.example.skripsi.ui.course

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.skripsi.MyApp
import com.example.skripsi.R
import com.example.skripsi.data.model.Question
import com.example.skripsi.data.repository.CourseRepository
import com.example.skripsi.ui.course.quiz.multiplechoice.MultipleChoiceFragment
import kotlinx.coroutines.launch

class QuizRunnerActivity : AppCompatActivity() {

    private var questions: List<Question> = emptyList()
    private var currentQuestionIndex = 0
    private val answers = mutableMapOf<Long, String>()

    private lateinit var btnNext: Button
    private lateinit var btnPrevious: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_runner)

        btnNext = findViewById(R.id.btnNext)
        btnPrevious = findViewById(R.id.btnPrevious)

        btnNext.setOnClickListener { nextQuestion() }
        btnPrevious.setOnClickListener { previousQuestion() }

        val quizId = intent.getLongExtra("quizId", -1L)
        if (quizId == -1L) {
            Log.e("QuizRunnerActivity", "No quizId received")
            finish()
            return
        }

        lifecycleScope.launch {
            val repo = CourseRepository(MyApp.supabase)
            questions = repo.getQuestionsByQuizId(quizId)

            if (questions.isEmpty()) {
                Log.e("QuizRunnerActivity", "No questions found for quizId $quizId")
                finish()
                return@launch
            }

            showQuestion(currentQuestionIndex)
            updateButtonState()
        }
    }

    private fun showQuestion(index: Int){
        if (index >= questions.size) {
            Log.i("QuizRunnerActivity", "Quiz Finished")
            finish() // TODO: Add completion screen
            return
        }

        val question = questions[index]
        val savedAnswer = answers[question.id]
        val fragment = when (question.questionType) {
            "match" -> MatchFragment.newInstance(question)
            "fill_in_the_blank" -> FillInTheBlankFragment() // TODO: add newInstance() method here
            "multiple_choice" -> MultipleChoiceFragment.newInstance(question, savedAnswer) // TODO: add newInstance() method here
            else -> {
                Log.e("QuizRunnerActivity", "Unknown question type: ${question.questionType}")
                return
            }
        }

        supportFragmentManager.commit {
            replace(R.id.quizFragmentContainer, fragment)
        }

        updateButtonState()
    }

    private fun nextQuestion() {
        if (currentQuestionIndex < questions.lastIndex) {
            currentQuestionIndex++
            showQuestion(currentQuestionIndex)
        }
    }

    private fun previousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--
            showQuestion(currentQuestionIndex)
        }
    }

    private fun updateButtonState() {
        btnPrevious.isEnabled = currentQuestionIndex > 0
        btnNext.isEnabled = currentQuestionIndex < questions.lastIndex
    }


    fun onAnswerSelected(questionId: Long, selectedAnswer: String) {
        answers[questionId] = selectedAnswer
    }

    fun continueQuestion() {
        currentQuestionIndex++
        showQuestion(currentQuestionIndex)
    }
}
