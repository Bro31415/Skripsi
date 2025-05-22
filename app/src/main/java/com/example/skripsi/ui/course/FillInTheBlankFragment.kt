package com.example.skripsi.ui.course

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.R
import com.example.skripsi.data.model.Question
import com.example.skripsi.ui.course.quiz.fillintheblank.FillInTheBlankViewModelFactory
import com.example.skripsi.ui.screens.FillInTheBlankScreen
import com.example.skripsi.viewmodel.FillInTheBlankViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FillInTheBlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FillInTheBlankFragment : Fragment() {

    private var question: Question? = null
    private var savedAnswer: String? = null
    private lateinit var viewModel: FillInTheBlankViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { bundle ->
            question = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(ARG_QUESTION, Question::class.java)
            } else {
                @Suppress("DEPRECATION")
                bundle.getParcelable(ARG_QUESTION)
            }

            savedAnswer = bundle.getString(ARG_SAVED_ANSWER)
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
                    FillInTheBlankScreen(viewModel = viewModel)
                }
            }
        }
    }

    companion object {
        private const val ARG_QUESTION = "arg_question"
        private const val ARG_SAVED_ANSWER = "arg_saved_answer"

        fun newInstance(question: Question, savedAnswer: String?): FillInTheBlankFragment {
            return FillInTheBlankFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_QUESTION, question)
                    putString(ARG_SAVED_ANSWER, savedAnswer)
                }
            }
        }
    }
}
