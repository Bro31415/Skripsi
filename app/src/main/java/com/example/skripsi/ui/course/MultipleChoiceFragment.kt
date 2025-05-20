package com.example.skripsi.ui.course.quiz.multiplechoice

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.skripsi.data.model.Question
import com.example.skripsi.ui.course.QuizRunnerActivity

class MultipleChoiceFragment : Fragment() {

    companion object {
        private const val ARG_QUESTION = "arg_question"
        private const val ARG_SAVED_ANSWER = "arg_saved_answer"


        fun newInstance(question: Question, savedAnswer: String?): MultipleChoiceFragment {
            return MultipleChoiceFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_QUESTION, question)
                    putString(ARG_SAVED_ANSWER, savedAnswer)
                }
            }
        }
    }

    private val question: Question by lazy {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(ARG_QUESTION, Question::class.java)!!
        } else {
            requireArguments().getParcelable(ARG_QUESTION)!!
        }
    }

    private val savedAnswer: String? by lazy {
        requireArguments().getString(ARG_SAVED_ANSWER)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    MultipleChoiceScreen(
                        question = question,
                        savedAnswer = savedAnswer,
                        onAnswerSelected = { selected ->
                            (activity as? QuizRunnerActivity)?.onAnswerSelected(question.id, selected)
                        }
                    )
                }
            }
        }
    }
}
