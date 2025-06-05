package com.example.skripsi.ui.leaderboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skripsi.R
import com.example.skripsi.data.model.User
import com.example.skripsi.data.repository.UserRepository
import kotlinx.coroutines.launch

class LeaderboardFragment : Fragment() {

    private lateinit var leaderboardRecyclerView: RecyclerView
    private val userRepository = UserRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        leaderboardRecyclerView = view.findViewById(R.id.leaderboardRecyclerView)
        leaderboardRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadLeaderboard()
    }

    private fun loadLeaderboard() {
        lifecycleScope.launch {
            try {
                val users = userRepository.getTopUsers(limit = 15)
                leaderboardRecyclerView.adapter = LeaderboardAdapter(users)
            } catch (e: Exception) {
                Log.e("Leaderboard", "Failed to load leaderboard: ${e.message}")
            }
        }
    }
}
