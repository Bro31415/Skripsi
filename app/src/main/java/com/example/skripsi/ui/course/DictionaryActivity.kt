package com.example.skripsi.ui.course

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.MyApp
import com.example.skripsi.data.repository.DictionaryRepository
import com.example.skripsi.viewmodel.DictionaryViewModel
import com.example.skripsi.viewmodel.factory.DictionaryViewModelFactory
import java.util.Locale

class DictionaryActivity : ComponentActivity(), TextToSpeech.OnInitListener {

    private val viewModel: DictionaryViewModel by viewModels {
        val supabase = MyApp.supabase
        val repository = DictionaryRepository(supabase)
        DictionaryViewModelFactory(repository)
    }

    private lateinit var tts: TextToSpeech
    private var isTtsReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val chapterId = intent.getLongExtra("chapterId", -1)

        tts = TextToSpeech(this, this)

        if (chapterId != -1L) {
            viewModel.fetchEntries(chapterId)
        }

        setContent {
            DictionaryScreen(
                viewModel = viewModel,
                onSpeak = { textToSpeak ->
                    speak(textToSpeak)
                }
            )
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val sundaneseLocale = Locale("su", "ID")
            val result = tts.setLanguage(sundaneseLocale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS_Dictionary", "Bahasa Sunda tidak didukung atau perlu diunduh.")
                isTtsReady = false
                Toast.makeText(this, "Unduh data sora Sunda di Setélan TTS Anjeun", Toast.LENGTH_LONG).show()
            } else {
                Log.i("TTS_Dictionary", "TTS siap di DictionaryActivity.")
                isTtsReady = true
            }
        } else {
            Log.e("TTS_Dictionary", "Inisialisasi TTS Gagal!")
            isTtsReady = false
        }
    }

    private fun speak(text: String) {
        if (isTtsReady && text.isNotBlank()) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        } else if (!isTtsReady) {
            Log.e("TTS_Dictionary", "TTS belum siap.")
            Toast.makeText(this, "Fitur sora teu acan sayogi", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            if (tts.isSpeaking) {
                tts.stop()
            }
            tts.shutdown()
        }
        super.onDestroy()
    }
}