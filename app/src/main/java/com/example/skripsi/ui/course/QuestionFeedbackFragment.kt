package com.example.skripsi.ui.course

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.skripsi.R

class QuestionFeedbackFragment : Fragment() {

    companion object {
        private const val ARG_IS_CORRECT = "is_correct"

        fun newInstance(isCorrect: Boolean): QuestionFeedbackFragment {
            val fragment = QuestionFeedbackFragment()
            fragment.arguments = bundleOf(ARG_IS_CORRECT to isCorrect)
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val isCorrect = arguments?.getBoolean(ARG_IS_CORRECT) ?: false
        val layoutId = if (isCorrect) {
            Log.i("QuestionFeedback", "Answer is correct")
            R.layout.fragment_correct_feedback
        } else {
            Log.i("QuestionFeedback", "Answer is wrong")
            R.layout.fragment_wrong_feedback
        }
        return inflater.inflate(layoutId, container, false)
    }

}