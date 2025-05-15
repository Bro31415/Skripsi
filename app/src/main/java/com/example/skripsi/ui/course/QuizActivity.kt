package com.example.skripsi.ui.course

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.skripsi.R
import com.example.skripsi.data.repository.QuizRepository
import kotlinx.coroutines.launch
import kotlin.reflect.jvm.internal.impl.incremental.components.Position

class QuizActivity : AppCompatActivity() {

    private val quizRepository = QuizRepository()
    private lateinit var container: LinearLayout
    private var chapterId: Long = 1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz)

        container = findViewById(R.id.quizButtonContainer)

        lifecycleScope.launch {
            try {
                val quizTypes = quizRepository.getQuizTypesChapter(chapterId)

                quizTypes.forEach { quizType ->
                    val button = Button(this@QuizActivity).apply {
                        text = quizType
                        setOnClickListener {
                            lifecycleScope.launch {
                                try {
                                    val quizId = quizRepository.getQuizId(chapterId, quizType)
                                    val questions = quizRepository.getQuizQuestions(quizId)

                                    val intent = Intent(this@QuizActivity, QuizRunnerActivity::class.java).apply {
                                        putExtra("quizType", quizType)
                                        putExtra("quizId", quizId)
                                        putParcelableArrayListExtra("questions", ArrayList(questions))
                                    }

                                    startActivity(intent)
                                } catch (e: Exception) {
                                    Log.e("QuizActivity", "Error loading quiz: ${e.message}", e)
                                    Toast.makeText(this@QuizActivity, "Failed to load quiz", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                    container.addView(button)
                }
            } catch (e: Exception) {
                Log.e("QuizActivity", "Failed to fetch quiz types: ${e.message}", e)
                Toast.makeText(this@QuizActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
        showQuestion(0) // Hardcoded
    }

    private fun showQuestion(position: Int) {
        val fragment = when (position) {
            0 -> MatchFragment()
            1 -> FillInTheBlankFragment()
            2 -> MultipleChoiceFragment()
            else -> throw IllegalStateException("Invalid Position")
        }

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).commit()
    }
}
