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

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = QuizAdapter(this)
    }
}

class QuizAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3 // ini hardcoded sementara untuk testing fragments--nanti diset untuk di databasenya gimana

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MultipleChoiceFragment()
            1 -> FillInTheBlankFragment()
            2 -> MatchFragment()
            else -> throw IllegalStateException()
        }
    }
}