package com.example.skripsi.ui.course

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.MyApp
import com.example.skripsi.data.repository.DictionaryRepository
import com.example.skripsi.viewmodel.DictionaryViewModel
import com.example.skripsi.viewmodel.factory.DictionaryViewModelFactory

class DictionaryActivity : ComponentActivity() {

    private val viewModel: DictionaryViewModel by viewModels {
        val supabase = MyApp.supabase
        val repository = DictionaryRepository(supabase)
        DictionaryViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val chapterId = intent.getLongExtra("chapterId", -1)

        if (chapterId != -1L) {
            viewModel.fetchEntries(chapterId)
        }

        setContent {
            DictionaryScreen(viewModel = viewModel)
        }
    }
}