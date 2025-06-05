package com.example.skripsi.ui.course

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.data.model.Question
import com.example.skripsi.ui.screens.FillInTheBlankScreen
import com.example.skripsi.ui.course.quiz.fillintheblank.FillInTheBlankViewModelFactory
import com.example.skripsi.viewmodel.FillInTheBlankViewModel

class FillInTheBlankFragment : Fragment() {

    private var question: Question? = null
    private lateinit var viewModel: FillInTheBlankViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        question = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable("question", Question::class.java)
        } else {
            @Suppress("DEPRECATION")
            requireArguments().getParcelable("question")
        }

        if (question == null) {
            throw IllegalArgumentException("Question argument is required")
        }

        viewModel = ViewModelProvider(
            this,
            FillInTheBlankViewModelFactory(question!!)
        )[FillInTheBlankViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    FillInTheBlankScreen(viewModel) { isCorrect ->
                        (activity as? QuizRunnerActivity)?.submitAnswer(isCorrect)
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(question: Question): FillInTheBlankFragment {
            val fragment = FillInTheBlankFragment()
            val args = Bundle()
            args.putParcelable("question", question)
            fragment.arguments = args
            return fragment
        }
    }
}
