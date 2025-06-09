package com.example.skripsi.ui.course.quiz.multiplechoice

import MultipleChoiceViewModel
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.skripsi.data.model.Question
import com.example.skripsi.ui.course.QuizRunnerActivity
import com.example.skripsi.ui.theme.AppTheme

class MultipleChoiceFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val question = arguments?.getParcelable<Question>("question")
            ?: throw IllegalStateException("Question cannot be null")

        return ComposeView(requireContext()).apply {
            setContent {
                val activity = LocalContext.current as? QuizRunnerActivity
                val factory = MultipleChoiceViewModelFactory(question)
                val viewModel: MultipleChoiceViewModel = viewModel(factory = factory)
                val uiState = viewModel.uiState

                AppTheme {
                    MultipleChoiceScreen(
                        uiState = uiState,
                        onAnswerSelected = viewModel::onAnswerSelected,
                        onSubmit = {
                            viewModel.onSubmit()
                            viewModel.uiState.isCorrect?.let { isCorrect ->
                                activity?.submitAnswer(isCorrect)
                            }
                        }
                    )
                }
            }
        }
    }

    companion object {
        fun newInstance(question: Question): MultipleChoiceFragment {
            return MultipleChoiceFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("question", question)
                }
            }
        }
    }
}


