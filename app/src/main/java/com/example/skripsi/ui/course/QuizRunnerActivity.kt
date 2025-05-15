package com.example.skripsi.ui.course

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.lifecycleScope
import com.example.skripsi.MyApp
import com.example.skripsi.R
import com.example.skripsi.data.model.Question
import com.example.skripsi.data.repository.CourseRepository
import kotlinx.coroutines.launch

class QuizRunnerActivity : AppCompatActivity() {

    private var questions: List<Question> = emptyList()
    private var currentQuestionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_runner)

        val quizId = intent.getLongExtra("quizId", -1L)
        if (quizId == -1L) {
            Log.e("QuizRunnerActivity", "No quizId received")
            finish()
            return
        }

        lifecycleScope.launch {
            // Assuming you have a repository to fetch questions by quizId
            val repo = CourseRepository(MyApp.supabase)
            questions = repo.getQuestionsByQuizId(quizId)

            if (questions.isEmpty()) {
                Log.e("QuizRunnerActivity", "No questions found for quizId $quizId")
                finish()
                return@launch
            }

            showQuestion(currentQuestionIndex)
        }
    }

    private fun showQuestion(index: Int){
        if (index >= questions.size) {
            Log.i("QuizRunnerActivity", "Quiz Finished")
            finish() // TODO: Add completion screen
            return
        }

        val question = questions[index]
        val fragment = when (question.questionType) {
            "match" -> MatchFragment.newInstance(question)
            "fill_in_the_blank" -> FillInTheBlankFragment() // TODO: add newInstance() method here
            "multiple_choice" -> MultipleChoiceFragment() // TODO: add newInstance() method here
            else -> {
                Log.e("QuizRunnerActivity", "Unknown question type: ${question.questionType}")
                return
            }
        }

        supportFragmentManager.commit {
            replace(R.id.quizFragmentContainer, fragment)
        }
    }

    fun continueQuestion() {
        currentQuestionIndex++
        showQuestion(currentQuestionIndex)
    }
}
