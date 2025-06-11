package com.example.skripsi.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skripsi.data.model.Chapter
import com.example.skripsi.data.model.Dictionary
import com.example.skripsi.data.repository.CourseRepository
import com.example.skripsi.data.repository.DictionaryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChapterWithWords(
    val chapter: Chapter,
    val words: List<Dictionary>
)

class DictionaryViewModel(
    private val courseRepository: CourseRepository,
    private val dictionaryRepository: DictionaryRepository
) : ViewModel() {

    private val _chapter = MutableStateFlow<Chapter?>(null)
    val chapter = _chapter.asStateFlow()

    private val _words = MutableStateFlow<List<Dictionary>>(emptyList())
    val words = _words.asStateFlow()

    fun loadDictionaryForChapter(chapterId: Long) {
        if (chapterId == -1L) return

        viewModelScope.launch {
            try {
                _chapter.value = courseRepository.getChapterById(chapterId)
                val wordsList = dictionaryRepository.getDictionaryByChapterId(chapterId)
                _words.value = wordsList
            } catch (e: Exception) {
                Log.e("DictionaryViewModel", "Failed to load dictionary for chapter $chapterId", e)
            }
        }
    }

    fun getWordById(wordId: Long): Dictionary? {
        return _words.value.find { it.id == wordId }
    }
}
