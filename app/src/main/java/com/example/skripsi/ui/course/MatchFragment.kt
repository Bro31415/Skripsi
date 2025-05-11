package com.example.skripsi.ui.course

import android.content.ClipData.Item
import android.os.Build
import android.os.Bundle
import android.service.autofill.FieldClassification.Match
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skripsi.R
import com.example.skripsi.data.model.Question
import com.example.skripsi.ui.screens.MatchScreen
import java.util.Collections

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class MatchFragment : Fragment() {

    private var question: Question? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            question = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getParcelable("question", Question::class.java)
            } else {
                @Suppress("DEPRECATION")
                arguments?.getParcelable("question")
            }

        }

        if (question == null) {
            Log.e("MatchFragment", "Question argument is missing")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    MatchScreen()
                }
            }
        }
    }

    companion object {
        fun newInstance(question: Question): MatchFragment {
            val fragment = MatchFragment()
            val args = Bundle()
            args.putParcelable("question", question)
            fragment.arguments = args

            return fragment
        }
    }
}