package com.example.skripsi.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.data.repository.CourseRepository
import com.example.skripsi.data.repository.DictionaryRepository
import com.example.skripsi.viewmodel.DictionaryViewModel

class DictionaryViewModelFactory(
    private val courseRepository: CourseRepository,
    private val dictionaryRepository: DictionaryRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DictionaryViewModel::class.java)) {
            // When creating the ViewModel, pass both repositories into its constructor
            @Suppress("UNCHECKED_CAST")
            return DictionaryViewModel(courseRepository, dictionaryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
