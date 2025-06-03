package com.example.skripsi.ui.course

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.skripsi.R
import com.example.skripsi.ui.HomeActivity

class QuizResultActivity : AppCompatActivity() {
    private val SPLASH_DISPLAY_LENGTH: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val firstTryUnlocked = intent.getBooleanExtra("wasFirstTryAchievementUnlocked", false)
        val xpHunterUnlocked = intent.getBooleanExtra("wasXpHunterAchievementUnlocked", false)
        if (firstTryUnlocked || xpHunterUnlocked) {
            showAchievementSplash(firstTryUnlocked, xpHunterUnlocked)
        } else {
            showQuizResultContent()
        }

    }
    private fun showAchievementSplash(firstTryUnlocked: Boolean, xpHunterUnlocked: Boolean) {
        if (firstTryUnlocked && xpHunterUnlocked) {
            setContentView(R.layout.first_try_splash)
            Handler(Looper.getMainLooper()).postDelayed({
                setContentView(R.layout.xp_hunter_splash)
                Handler(Looper.getMainLooper()).postDelayed({
                    showQuizResultContent()
                }, SPLASH_DISPLAY_LENGTH)
            }, SPLASH_DISPLAY_LENGTH)
        } else if (firstTryUnlocked){
            setContentView(R.layout.first_try_splash)
            Handler(Looper.getMainLooper()).postDelayed({
                showQuizResultContent()
            }, SPLASH_DISPLAY_LENGTH)
        } else if (xpHunterUnlocked){
            setContentView(R.layout.xp_hunter_splash)
            Handler(Looper.getMainLooper()).postDelayed({
                showQuizResultContent()
            }, SPLASH_DISPLAY_LENGTH)
        } else {
            showQuizResultContent()
        }


    }

    private fun showQuizResultContent() {
        setContentView(R.layout.activity_quiz_result)

        val totalXp = intent.getIntExtra("totalXp", 0)
        val resultText = findViewById<TextView>(R.id.xpText)
        val backButton = findViewById<Button>(R.id.backButton)


        resultText.text = "Total XP Earned: $totalXp"

        backButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}