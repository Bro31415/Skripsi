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
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit

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
        val konfettiView = findViewById<KonfettiView>(R.id.konfettiView)
        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
            position = Position.Relative(0.5, 0.3)
        )

        if (firstTryUnlocked && xpHunterUnlocked) {
            setContentView(R.layout.first_try_splash)
            findViewById<KonfettiView>(R.id.konfettiView).start(party)
            Handler(Looper.getMainLooper()).postDelayed({
                setContentView(R.layout.xp_hunter_splash)
                findViewById<KonfettiView>(R.id.konfettiView).start(party)
                Handler(Looper.getMainLooper()).postDelayed({
                    showQuizResultContent()
                }, SPLASH_DISPLAY_LENGTH)
            }, SPLASH_DISPLAY_LENGTH)
        } else if (firstTryUnlocked){
            setContentView(R.layout.first_try_splash)
            findViewById<KonfettiView>(R.id.konfettiView).start(party)
            Handler(Looper.getMainLooper()).postDelayed({
                showQuizResultContent()
            }, SPLASH_DISPLAY_LENGTH)
        } else if (xpHunterUnlocked){
            setContentView(R.layout.xp_hunter_splash)
            findViewById<KonfettiView>(R.id.konfettiView).start(party)
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
        val konfettiView = findViewById<KonfettiView>(R.id.konfettiView)

        resultText.text = "$totalXp"

        backButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
            position = Position.Relative(0.5, 0.3)
        )
        konfettiView.start(party)
    }
}