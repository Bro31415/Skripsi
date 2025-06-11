import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skripsi.R

class ProfileAchievementsAdapter : ListAdapter<Achievement, ProfileAchievementsAdapter.ViewHolder>(AchievementDiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val icon: ImageView = view.findViewById(R.id.iv_achievement_icon)
        private val name: TextView = view.findViewById(R.id.tv_achievement_name)

        fun bind(achievement: Achievement) {
            name.text = achievement.name
            // Since the Achievement model doesn't have a specific icon, we'll use a static trophy icon
            icon.setImageResource(R.drawable.bold_cup)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_achievement_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

// DiffUtil helps RecyclerView update the list efficiently
class AchievementDiffCallback : DiffUtil.ItemCallback<Achievement>() {
    override fun areItemsTheSame(oldItem: Achievement, newItem: Achievement): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Achievement, newItem: Achievement): Boolean {
        return oldItem.name == newItem.name && oldItem.description == newItem.description
    }
}