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
import com.example.skripsi.ui.course.quiz.fillintheblank.FillBlankViewModelFactory
import com.example.skripsi.ui.screens.FillBlankScreen
import com.example.skripsi.viewmodel.FillBlankViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FillBlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FillBlankFragment : Fragment() {

    private var question: Question? = null
    private lateinit var viewModel: FillBlankViewModel

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
            FillBlankViewModelFactory(question!!)
        )[FillBlankViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    FillBlankScreen(viewModel = viewModel)
                }
            }
        }
    }

    companion object {
        fun newInstance(question: Question): FillBlankFragment {
            val fragment = FillBlankFragment()
            val args = Bundle()
            args.putParcelable("question", question)
            fragment.arguments = args
            return fragment
        }
    }
}
