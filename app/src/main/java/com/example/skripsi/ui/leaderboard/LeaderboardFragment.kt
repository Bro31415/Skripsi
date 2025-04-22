package com.example.skripsi.ui.leaderboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.skripsi.R
import com.example.skripsi.data.model.User
import com.example.skripsi.data.repository.UserRepository
import kotlinx.coroutines.launch

class LeaderboardFragment : Fragment() {

    // User repository (you'll need to inject or create this based on your app architecture)
    private lateinit var userRepository: UserRepository

    // View references
    private lateinit var playerNames: Array<TextView>
    private lateinit var playerXPs: Array<TextView>
    private lateinit var relegationPlayerName: TextView
    private lateinit var relegationPlayerXP: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize user repository
        userRepository = UserRepository()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment layout
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize view references
        playerNames = arrayOf(
            view.findViewById(R.id.player_name1),
            view.findViewById(R.id.player_name2),
            view.findViewById(R.id.player_name3),
            view.findViewById(R.id.player_name4)
        )

        playerXPs = arrayOf(
            view.findViewById(R.id.player_xp1),
            view.findViewById(R.id.player_xp2),
            view.findViewById(R.id.player_xp3),
            view.findViewById(R.id.player_xp4)
        )

        relegationPlayerName = view.findViewById(R.id.relegation_player_name)
        relegationPlayerXP = view.findViewById(R.id.relegation_player_xp)

        // Load leaderboard data
        loadLeaderboardData()
    }

    private fun loadLeaderboardData() {
        // Add initial log message
        Log.d("LeaderboardFragment", "Starting to load leaderboard data")

        lifecycleScope.launch {
            try {
                // Fetch all users from the database
                Log.d("LeaderboardFragment", "Attempting to fetch users")
                val allUsers = userRepository.getAllUsers()

                // Log the result
                Log.d("LeaderboardFragment", "Fetched ${allUsers.size} users")
                if (allUsers.isEmpty()) {
                    Log.d("LeaderboardFragment", "No users found in database")
                    // Display some UI indication that no data is available
                    playerNames[0].text = "No users found"
                    return@launch
                }

                // Sort users by XP in descending order
                val sortedUsers = allUsers.sortedByDescending { it.xp }
                Log.d("LeaderboardFragment", "Sorted users, top user: ${sortedUsers.firstOrNull()?.username}")

                // Update UI with sorted users
                displayLeaderboard(sortedUsers)
                Log.d("LeaderboardFragment", "Displayed leaderboard data")
            } catch (e: Exception) {
                Log.e("LeaderboardFragment", "Error loading leaderboard data", e)
                // Display error message in UI
                playerNames[0].text = "Error loading data"
            }
        }
    }

    private fun displayLeaderboard(users: List<User>) {
        // Make sure we have users to display
        if (users.isEmpty()) return

        // Fill in the top 4 players
        for (i in 0 until minOf(4, users.size)) {
            playerNames[i].text = users[i].username
            playerXPs[i].text = "${users[i].xp} XP"
        }

        // Get relegation zone player (5th player or last if less than 5)
        val relegationIndex = if (users.size > 4) 4 else users.size - 1
        if (relegationIndex >= 0 && relegationIndex < users.size) {
            relegationPlayerName.text = users[relegationIndex].username
            relegationPlayerXP.text = "${users[relegationIndex].xp} XP"
        }
    }
}