package com.example.skripsi.ui.course

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.skripsi.R
import com.example.skripsi.data.model.Question

class QuizRunnerActivity : AppCompatActivity() {

    private lateinit var questions: List<Question>
    private var currentQuestionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_runner)

        questions = intent.getParcelableArrayListExtra("questions") ?: emptyList()

        if (questions.isEmpty()) {
            Log.e("QuizRunnerActivity", "No questions received from quiz activity")
            finish()
            return
        }

        showQuestion(currentQuestionIndex)
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
            "fill_in_the_blank" -> FillInTheBlankFragment() // TODO: Add newInstance() inside fragment
            "multiple_choice" -> MultipleChoiceFragment() // TODO: Add newInstance() inside fragment
            else -> {
                Log.e("QuizRunnerActivity", "Unknown question type: ${question.questionType}")
                return
            }
        }

        supportFragmentManager.commit {
            replace(R.id.qu)
        }
    }
}