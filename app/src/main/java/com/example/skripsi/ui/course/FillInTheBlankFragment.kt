package com.example.skripsi.ui.course

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.skripsi.data.model.Question
import com.example.skripsi.ui.course.quiz.fillintheblank.FillInTheBlankViewModelFactory
import com.example.skripsi.ui.quiz.FillInTheBlankScreen
import com.example.skripsi.ui.theme.AppTheme
import com.example.skripsi.viewmodel.FillInTheBlankViewModel

class FillInTheBlankFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val question = arguments?.getParcelable<Question>("question")
            ?: throw IllegalStateException("Question cannot be null")

        return ComposeView(requireContext()).apply {
            setContent {
                val activity = LocalContext.current as? QuizRunnerActivity
                val factory = FillInTheBlankViewModelFactory(question)
                val viewModel: FillInTheBlankViewModel = viewModel(factory = factory)

                AppTheme {
                    FillInTheBlankScreen(
                        uiState = viewModel.uiState,
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
        fun newInstance(question: Question): FillInTheBlankFragment {
            return FillInTheBlankFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("question", question)
                }
            }
        }
    }
}
