package com.example.skripsi.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.data.repository.DictionaryRepository
import com.example.skripsi.viewmodel.DictionaryViewModel

class DictionaryViewModelFactory(private val repository: DictionaryRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DictionaryViewModel(repository) as T
    }
}
