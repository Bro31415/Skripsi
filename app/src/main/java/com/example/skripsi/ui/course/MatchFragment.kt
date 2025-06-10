package com.example.skripsi.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.skripsi.data.model.Question
import com.example.skripsi.ui.course.QuizRunnerActivity
import com.example.skripsi.ui.course.quiz.matchwords.MatchViewModelFactory
import com.example.skripsi.ui.theme.AppTheme
import com.example.skripsi.viewmodel.MatchViewModel

class MatchFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val question = arguments?.getParcelable<Question>("question")
            ?: throw IllegalStateException("Question cannot be null")

        return ComposeView(requireContext()).apply {
            setContent {
                val activity = LocalContext.current as? QuizRunnerActivity
                val factory = MatchViewModelFactory(question)
                val viewModel: MatchViewModel = viewModel(factory = factory)

                AppTheme {
                    // It simply calls the composable, which is now imported from the other file.
                    MatchScreen(
                        uiState = viewModel.uiState,
                        onWordBankChipClicked = viewModel::onWordBankChipClicked,
                        onAnswerChipClicked = viewModel::onAnswerChipClicked,
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
        fun newInstance(question: Question): MatchFragment {
            return MatchFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("question", question)
                }
            }
        }
    }
}