package com.example.skripsi.ui


import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.skripsi.R
import com.example.skripsi.ui.leaderboard.LeaderboardFragment
import com.example.skripsi.ui.course.CourseFragment

import com.example.skripsi.ui.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val bottomNavBar = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CourseFragment()).commit()
        }

        bottomNavBar.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.course -> CourseFragment()
                R.id.leaderboard -> LeaderboardFragment()
                R.id.profile -> ProfileFragment()
                R.id.settings -> SettingsFragment()
                else -> null
            }
            if (fragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
                true
            } else {
                false
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }
}