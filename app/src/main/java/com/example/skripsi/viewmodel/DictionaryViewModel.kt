package com.example.skripsi.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skripsi.data.model.Dictionary
import com.example.skripsi.data.repository.DictionaryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DictionaryViewModel(private val repository: DictionaryRepository) : ViewModel() {

    private val _entries = MutableStateFlow<List<Dictionary>>(emptyList())
    val entries: StateFlow<List<Dictionary>> = _entries

    fun fetchEntries(chapterId: Long) {
        viewModelScope.launch {
            try {
                val result = repository.getDictionaryByChapterId(chapterId)
                _entries.value = result
            } catch (e: Exception) {
                Log.e("DictionaryViewModel", "Failed to load dictionary", e)
            }
        }
    }
}
