package com.example.skripsi.ui.leaderboard

import LeaderboardViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skripsi.R
import com.example.skripsi.data.model.User
import com.example.skripsi.data.repository.UserRepository
import com.example.skripsi.viewmodel.LeaderboardViewModel

class LeaderboardFragment : Fragment() {

    private val viewModel: LeaderboardViewModel by viewModels {
        LeaderboardViewModelFactory(UserRepository())
    }
    private lateinit var leaderboardRecyclerView: RecyclerView
    private lateinit var adapter: LeaderboardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_leaderboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(view)

        viewModel.topUsers.observe(viewLifecycleOwner) { users ->
            if (users.isNotEmpty()) {
                val topThree = users.take(3)
                val restOfUsers = if (users.size > 3) users.subList(3, users.size) else emptyList()

                bindTopThree(view, topThree)
                adapter = LeaderboardAdapter(restOfUsers, viewModel.currentUserId)
                leaderboardRecyclerView.adapter = adapter
            }
        }
    }

    private fun setupRecyclerView(view: View) {
        leaderboardRecyclerView = view.findViewById(R.id.leaderboardRecyclerView)
        leaderboardRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun bindTopThree(view: View, topUsers: List<User>) {
        val rank1Layout = view.findViewById<ConstraintLayout>(R.id.layout_rank_1)
        val rank2Layout = view.findViewById<ConstraintLayout>(R.id.layout_rank_2)
        val rank3Layout = view.findViewById<ConstraintLayout>(R.id.layout_rank_3)

        if (topUsers.isNotEmpty()) {
            bindSingleTopUser(rank1Layout, topUsers[0], R.drawable.asset_crown)
        }

        if (topUsers.size >= 2) {
            bindSingleTopUser(rank2Layout, topUsers[1], R.drawable.bold_medal_star)
        }
        if (topUsers.size >= 3) {
            bindSingleTopUser(rank3Layout, topUsers[2], R.drawable.bold_medal_star_circle)
        }
    }
    private fun bindSingleTopUser(layout: View, user: User, iconRes: Int) {
        val rankIcon = layout.findViewById<ImageView>(R.id.iv_rank_icon)
        val avatar = layout.findViewById<com.google.android.material.imageview.ShapeableImageView>(R.id.iv_avatar)
        val username = layout.findViewById<TextView>(R.id.tv_username)
        val xp = layout.findViewById<TextView>(R.id.tv_xp)

        rankIcon.setImageResource(iconRes)
        username.text = user.username
        xp.text = "${user.xp ?: 0} XP"

        if (!user.user_photo_profile.isNullOrBlank()) {
            Glide.with(this)
                .load(user.user_photo_profile)
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile)
                .circleCrop()
                .into(avatar)
        } else {
            avatar.setImageResource(R.drawable.default_profile)
        }
    }
}