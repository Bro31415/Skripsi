package com.example.skripsi.ui.leaderboard

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.skripsi.R
import com.example.skripsi.data.model.User
import com.example.skripsi.data.repository.UserRepository
import kotlinx.coroutines.launch

class LeaderboardActivity : AppCompatActivity() {

    // User repository (you'll need to inject or create this based on your app architecture)
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_leaderboard)

        // Initialize user repository
        userRepository = UserRepository()

        // Load leaderboard data
        loadLeaderboardData()
    }

    private fun loadLeaderboardData() {
        lifecycleScope.launch {
            try {
                // Fetch all users from the database
                val allUsers = userRepository.getAllUsers()

                // Sort users by XP in descending order
                val sortedUsers = allUsers.sortedByDescending { it.xp }

                // Update UI with sorted users
                displayLeaderboard(sortedUsers)
            } catch (e: Exception) {
                // Handle any errors that might occur during data fetching
                e.printStackTrace()
                // Show error message to the user or implement retry logic
            }
        }
    }

    private fun displayLeaderboard(users: List<User>) {
        // Make sure we have users to display
        if (users.isEmpty()) return

        // Get references to TextViews for the main leaderboard (top 4 players)
        val playerNames = arrayOf(
            findViewById<TextView>(R.id.player_name1),
            findViewById<TextView>(R.id.player_name2),
            findViewById<TextView>(R.id.player_name3),
            findViewById<TextView>(R.id.player_name4)
        )

        val playerXPs = arrayOf(
            findViewById<TextView>(R.id.player_xp1),
            findViewById<TextView>(R.id.player_xp2),
            findViewById<TextView>(R.id.player_xp3),
            findViewById<TextView>(R.id.player_xp4)
        )

        // Fill in the top 4 players
        for (i in 0 until minOf(4, users.size)) {
            playerNames[i].text = users[i].username
            playerXPs[i].text = "${users[i].xp} XP"
        }

        // Get relegation zone player (5th player or last if less than 5)
        val relegationIndex = if (users.size > 4) 4 else users.size - 1
        if (relegationIndex >= 0 && relegationIndex < users.size) {
            findViewById<TextView>(R.id.relegation_player_name).text = users[relegationIndex].username
            findViewById<TextView>(R.id.relegation_player_xp).text = "${users[relegationIndex].xp} XP"
        }
    }
}

