// File: ui/adapter/AchievementAdapter.kt (buat folder adapter jika belum ada)
package com.example.skripsi.ui.course

import Achievement
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.skripsi.R


class AchievementAdapter(private val achievements: List<Achievement>) :
    RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder>() {

    class AchievementViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.tv_achievement_name)
        val descriptionTextView: TextView = view.findViewById(R.id.tv_achievement_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_achievement, parent, false)
        return AchievementViewHolder(view)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        val achievement = achievements[position]
        holder.nameTextView.text = achievement.name
        holder.descriptionTextView.text = achievement.description
    }

    override fun getItemCount() = achievements.size
}