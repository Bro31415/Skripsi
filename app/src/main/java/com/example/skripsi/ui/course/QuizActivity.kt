package com.example.skripsi.ui.course

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.skripsi.R
import kotlin.reflect.jvm.internal.impl.incremental.components.Position

class QuizActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        showQuestion(0) // Hardcoded
    }

    private fun showQuestion(position: Int) {
        val fragment = when (position) {
            0 -> MatchFragment()
            1 -> FillInTheBlankFragment()
            2 -> MultipleChoiceFragment()
            else -> throw IllegalStateException("Invalid Position")
        }

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).commit()
    }
}
