package com.example.skripsi.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.data.repository.CourseRepository
import com.example.skripsi.viewmodel.CourseViewModel
import io.github.jan.supabase.SupabaseClient

class CourseViewModelFactory (private val repository: CourseRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CourseViewModel(repository) as T
    }

}