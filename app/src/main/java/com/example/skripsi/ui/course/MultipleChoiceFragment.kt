package com.example.skripsi.ui.course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.skripsi.ui.quiz.multiplechoice.MultipleChoiceViewModel
import com.example.skripsi.ui.course.quiz.multiplechoice.MultipleChoiceScreen
import kotlinx.coroutines.launch

class MultipleChoiceFragment : Fragment() {

    private val viewModel: MultipleChoiceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val quizId = arguments?.getInt("quizId") ?: 1

        lifecycleScope.launch {
            viewModel.loadQuestions(quizId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return MultipleChoiceScreen(requireContext(), viewModel)
    }
}
