package com.example.skripsi.ui.course

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.skripsi.R

class QuestionFeedbackFragment : Fragment() {

    companion object {
        private const val ARG_IS_CORRECT = "is_correct"
        private const val ARG_SENTENCE = "sentence"

        fun newInstance(isCorrect: Boolean, sentence: String?): QuestionFeedbackFragment {
            val fragment = QuestionFeedbackFragment()
            fragment.arguments = bundleOf(ARG_IS_CORRECT to isCorrect, ARG_SENTENCE to sentence)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sentence = arguments?.getString(ARG_SENTENCE)

        val correctAnswerLayout: LinearLayout? = view.findViewById(R.id.correctAnswerLayout)
        val correctSentenceText: TextView? = view.findViewById(R.id.correctSentenceText)
        val listenButton: ImageButton? = view.findViewById(R.id.listenButton)

        if (!sentence.isNullOrBlank() && correctAnswerLayout != null && correctSentenceText != null && listenButton != null) {
            correctAnswerLayout.visibility = View.VISIBLE
            correctSentenceText.text = sentence

            listenButton.setOnClickListener {
                (activity as? QuizRunnerActivity)?.speakSentence(sentence)
            }
        }
        val continueButton: Button? = view.findViewById(R.id.continueButton)
        continueButton?.setOnClickListener {
            (activity as? QuizRunnerActivity)?.userWantsToContinue()
        }
    }

}