package com.example.skripsi.ui.course.quiz.state

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.skripsi.R
import com.example.skripsi.ui.course.QuizRunnerActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ErrorFragment : Fragment(R.layout.fragment_error) {
    companion object {
        private const val ARG_MESSAGE = "error_message"

        fun newInstance(message: String): ErrorFragment {
            val fragment = ErrorFragment()
            val bundle = Bundle()
            bundle.putString(ARG_MESSAGE, message)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val message = arguments?.getString(ARG_MESSAGE) ?: "Something went wrong"
        view.findViewById<TextView>(R.id.errorMessage).text = message

        view.findViewById<Button>(R.id.retryButton).setOnClickListener {

            (activity as? QuizRunnerActivity)?.retryFetchQuestions()
        }
    }
}