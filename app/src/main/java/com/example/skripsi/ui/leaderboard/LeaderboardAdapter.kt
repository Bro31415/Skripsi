package com.example.skripsi.ui.leaderboard // Or your package

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skripsi.R
import com.example.skripsi.data.model.User
import com.google.android.material.imageview.ShapeableImageView

class LeaderboardAdapter(
    private var users: List<User>,
    private val currentUserId: String
) : RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {

    class LeaderboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardRoot: CardView = itemView.findViewById(R.id.card_root)
        private val rankTextView: TextView = itemView.findViewById(R.id.tv_rank)
        private val avatarImageView: ShapeableImageView = itemView.findViewById(R.id.iv_avatar)
        private val usernameTextView: TextView = itemView.findViewById(R.id.tvUsername)
        private val xpTextView: TextView = itemView.findViewById(R.id.tvXp)

        fun bind(user: User, rank: Int, isCurrentUser: Boolean) {
            rankTextView.text = rank.toString()
            usernameTextView.text = user.username
            xpTextView.text = "${user.xp ?: 0} XP"
            if (isCurrentUser) {
                cardRoot.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.green))
            } else {
                cardRoot.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white))
            }

            if (!user.user_photo_profile.isNullOrBlank()) {
                Glide.with(itemView.context)
                    .load(user.user_photo_profile)
                    .placeholder(R.drawable.default_profile)
                    .error(R.drawable.default_profile)
                    .circleCrop()
                    .into(avatarImageView)
            } else {
                avatarImageView.setImageResource(R.drawable.default_profile)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leaderboard, parent, false)
        return LeaderboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val user = users[position]
        val rank = position + 4
        val isCurrentUser = user.id == currentUserId
        holder.bind(user, rank, isCurrentUser)
    }

    override fun getItemCount(): Int = users.size
}