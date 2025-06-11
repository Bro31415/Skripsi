package com.example.skripsi.ui.course

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.skripsi.MyApp
import com.example.skripsi.R
import com.example.skripsi.data.model.Question
import com.example.skripsi.data.repository.CourseRepository
import com.example.skripsi.data.repository.UserProgressRepository
import com.example.skripsi.ui.course.quiz.multiplechoice.MultipleChoiceFragment
import com.example.skripsi.ui.course.quiz.state.ErrorFragment
import com.example.skripsi.ui.course.quiz.state.LoadingFragment
import com.example.skripsi.ui.quiz.MatchFragment
import com.example.skripsi.viewmodel.QuizUiState
import com.example.skripsi.viewmodel.QuizViewModel
import com.example.skripsi.viewmodel.factory.QuizViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.speech.tts.TextToSpeech
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.material.progressindicator.LinearProgressIndicator
import java.util.Locale

class QuizRunnerActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

        private lateinit var quizViewModel: QuizViewModel
        private lateinit var quizId: String
        private var hasStartedQuiz = false
        private val answers = mutableMapOf<Long, String>()
        private lateinit var tts: TextToSpeech
        private var isTtsReady = false
        private lateinit var progressIndicator: LinearProgressIndicator
        private lateinit var backButton: ImageButton

        @RequiresApi(Build.VERSION_CODES.O)
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

            progressIndicator = findViewById(R.id.quizProgressBar)
            backButton = findViewById(R.id.backButton)

            backButton.setOnClickListener {
                finish() // TODO: ADD CONFIRMATION DIALOG
            }

            quizId = quizIdLong.toString()
            tts = TextToSpeech(this, this)

            val courseRepository = CourseRepository(MyApp.supabase)
            val userProgressRepository = UserProgressRepository(MyApp.supabase)

            quizViewModel = ViewModelProvider(
                this,
                QuizViewModelFactory(quizId, courseRepository, userProgressRepository)
            )[QuizViewModel::class.java]

            lifecycleScope.launch {
                quizViewModel.uiState.collect { state ->
                    when (state) {
                        is QuizUiState.Loading -> showLoading()
                        is QuizUiState.ShowQuestion -> showQuestion(state.question, state.index, state.total)
                        is QuizUiState.ShowFeedback -> showFeedbackScreen(state.isCorrect, state.correctSentence)
                        is QuizUiState.Finished -> showResultScreen(state.totalXp)
                        is QuizUiState.Error -> showErrorScreen(state.message)
                    }
                }
            }
        }

        private fun showQuestion(question: Question, index: Int, total: Int) {
            if (!hasStartedQuiz) {
                quizViewModel.startQuizTimer()
                hasStartedQuiz = true
            }

            progressIndicator.max = total
            progressIndicator.setProgressCompat(index + 1, true)

            val savedAnswer = answers[question.id]

            val fragment = when (question.questionType) {
                "match" -> MatchFragment.newInstance(question)
                "fill_in_the_blank" -> FillInTheBlankFragment.newInstance(question)
                "multiple_choice" -> MultipleChoiceFragment.newInstance(question)
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

        override fun onInit(status: Int) {
            if (status == TextToSpeech.SUCCESS) {
                val sundaneseLocale = Locale("su", "ID")
                val result = tts.setLanguage(sundaneseLocale)

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Bahasa Sunda tidak didukung atau perlu diunduh.")
                    isTtsReady = false
                    Toast.makeText(this, "Unduh data sora Sunda di Setélan TTS Anjeun", Toast.LENGTH_LONG).show()
                } else {
                    Log.i("TTS", "Mesin TTS siap dengan Bahasa Sunda.")
                    isTtsReady = true
                }
            } else {
                Log.e("TTS", "Inisialisasi TTS Gagal!")
                isTtsReady = false
            }
        }


        fun speakSentence(sentence: String?) {
            if (isTtsReady && !sentence.isNullOrBlank()) {
                tts.speak(sentence, TextToSpeech.QUEUE_FLUSH, null, null)
            } else if (sentence.isNullOrBlank()) {
                Log.w("TTS", "Kalimat kosong, tidak ada yang dibacakan.")
            }
            else {
                Log.e("TTS", "TTS belum siap.")
                Toast.makeText(this, "Fitur sora teu acan sayogi", Toast.LENGTH_SHORT).show()
            }
        }


        override fun onDestroy() {
            if (::tts.isInitialized) {
                tts.stop()
                tts.shutdown()
            }
            super.onDestroy()
        }

        private fun showFeedbackScreen(isCorrect: Boolean, sentence: String?) {
            val fragment = QuestionFeedbackFragment.newInstance(isCorrect, sentence)
            supportFragmentManager.commit {
                replace(R.id.quizFragmentContainer, fragment)
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showResultScreen(totalXp: Int) {
        quizViewModel.endQuizTimer()

        lifecycleScope.launch {
            quizViewModel.logQuizResultBlocking(totalXp)

            val wasFirstTryUnlocked = quizViewModel.checkAndUnlockFirstTryAchievement()
            val wasXpHunterUnlocked = quizViewModel.checkAndUnlockXpHunterAchievement()

            val intent = Intent(this@QuizRunnerActivity, QuizResultActivity::class.java)
            intent.putExtra("totalXp", totalXp)
            intent.putExtra("wasFirstTryAchievementUnlocked", wasFirstTryUnlocked)
            intent.putExtra("wasXpHunterAchievementUnlocked", wasXpHunterUnlocked)
            startActivity(intent)

            delay(300)
            finish()
        }
    }


    fun onAnswerSelected(questionId: Long, selectedAnswer: String) {
            answers[questionId] = selectedAnswer
        }

        fun submitAnswer(isCorrect: Boolean) {
            quizViewModel.handleAnswer(isCorrect)
        }

    fun userWantsToContinue() {
        quizViewModel.moveToNextQuestion()
    }

        fun retryFetchQuestions() {
//            quizViewModel.retryFetchQuestions()
        }




}
