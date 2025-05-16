package com.example.skripsi.ui.course

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.skripsi.R
import com.example.skripsi.data.model.QuestionType

class QuizActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        // Ambil quizId dari intent
        val quizId = intent.getIntExtra("quizId", 1)
        Toast.makeText(this, "Quiz ID: $quizId", Toast.LENGTH_SHORT).show()
        println("Received quizId: $quizId") // Cek di Logcat
        showQuestion(quizId, QuestionType.multiple_choice) // Kirim quizId ke fragment
    }

    private fun showQuestion(quizId: Int, questionType: QuestionType) {
        val fragment = when (questionType) {
//            0 -> MatchFragment()
//            1 -> FillInTheBlankFragment()
//            2 -> MultipleChoiceFragment().apply {
//                arguments = Bundle().apply {
//                putInt("quizId", quizId)
//                }
            QuestionType.multiple_choice -> MultipleChoiceFragment().apply { //buat testing
                arguments = Bundle().apply {
                    putInt("quizId", quizId)

                }
            }

            else -> throw IllegalStateException("Invalid Position")
        }

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .commit()
    }
}

