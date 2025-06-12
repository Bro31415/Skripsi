package com.example.skripsi.ui.course

import DictionaryDetailScreen
import DictionaryScreen
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.skripsi.MyApp
import com.example.skripsi.data.repository.CourseRepository
import com.example.skripsi.data.repository.DictionaryRepository
import com.example.skripsi.ui.theme.AppTheme
import com.example.skripsi.viewmodel.DictionaryViewModel
import com.example.skripsi.viewmodel.factory.DictionaryViewModelFactory
import java.util.Locale

class DictionaryActivity : ComponentActivity(), TextToSpeech.OnInitListener {

    private val viewModel: DictionaryViewModel by viewModels {
        DictionaryViewModelFactory(
            CourseRepository(MyApp.supabase),
            DictionaryRepository(MyApp.supabase)
        )
    }
    private lateinit var tts: TextToSpeech
    private var isTtsReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tts = TextToSpeech(this, this)

        val chapterId = intent.getLongExtra("chapterId", -1L)
        viewModel.loadDictionaryForChapter(chapterId)

        setContent {
            AppTheme {
                val navController = rememberNavController()
                val chapter by viewModel.chapter.collectAsState()
                val words by viewModel.words.collectAsState()

                NavHost(
                    navController = navController,
                    startDestination = "list" ,
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None },
                    popEnterTransition = { EnterTransition.None },
                    popExitTransition = { ExitTransition.None }
                ) {

                    composable("list") {
                        DictionaryScreen(
                            chapter = chapter,
                            words = words,
                            onWordClick = { wordId ->
                                navController.navigate("detail/$wordId")
                            },
                            onBackClick = {
                                finish()
                            }
                        )
                    }

                    composable(
                        route = "detail/{wordId}",
                        arguments = listOf(navArgument("wordId") { type = NavType.LongType })
                    ) { backStackEntry ->
                        val wordId = backStackEntry.arguments?.getLong("wordId")
                        val word = wordId?.let { viewModel.getWordById(it) }
                        DictionaryDetailScreen(
                            word = word,
                            onBackClick = { navController.popBackStack() },
                            onSpeak = { text -> speakSentence(text)}
                        )
                    }
                }
            }
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
}