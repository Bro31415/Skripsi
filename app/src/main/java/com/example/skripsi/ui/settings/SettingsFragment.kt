package com.example.skripsi.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skripsi.R

class SettingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_settings)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val settingsList = listOf(
            SettingsItem("Tema Aplikasi", R.drawable.ic_theme),
            SettingsItem("Ukuran Font", R.drawable.ic_font),
            SettingsItem("Notifikasi", R.drawable.ic_notifications),
            SettingsItem("Ganti Password", R.drawable.ic_password),
            SettingsItem("Reset Progress", R.drawable.ic_reset),
            SettingsItem("Keluar dari Aplikasi", R.drawable.ic_logout)
        )

        recyclerView.adapter = SettingsAdapter(settingsList) { selectedItem ->
            // Handle item click here (e.g., navigate to another fragment or show a dialog)
        }

        return view
    }
}

// Data model for settings items
data class SettingsItem(val title: String, val iconRes: Int)

// RecyclerView Adapter
class SettingsAdapter(
    private val items: List<SettingsItem>,
    private val onItemClick: (SettingsItem) -> Unit
) : RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder>() {

    inner class SettingsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: SettingsItem) {
            itemView.findViewById<TextView>(R.id.text_setting).text = item.title
            itemView.findViewById<ImageView>(R.id.icon_setting).setImageResource(item.iconRes)
            itemView.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_setting, parent, false)
        return SettingsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SettingsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
