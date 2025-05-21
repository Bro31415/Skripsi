package com.example.skripsi.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.MyApp
import com.example.skripsi.R
import com.example.skripsi.data.model.User
import com.example.skripsi.data.repository.UserRepository
import com.example.skripsi.viewmodel.AuthViewModel
import com.example.skripsi.viewmodel.factory.AuthViewModelFactory
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.UploadData
import io.ktor.http.ContentType
import java.util.UUID

class ProfileFragment : Fragment() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var profileImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        profileImageView = view.findViewById(R.id.iv_profile)
        val userRepository = UserRepository()
        authViewModel = ViewModelProvider(this, AuthViewModelFactory(userRepository)).get(AuthViewModel::class.java)

        val currentUser = MyApp.supabase.auth.currentUserOrNull()
        val userId = currentUser?.id

        if (userId != null) {
            loadUserProfile(userId, view)
        }

        return view
    }

    private fun loadUserProfile(userId: String, view: View) {
        Log.d("ProfileFragment", "User ID: $userId")
        authViewModel.getUserProfile(userId) { user ->
            if (user != null) {
                // Tampilkan username
                view.findViewById<TextView>(R.id.tv_username).text = user.username

                //Tampilkan XP
                val xpText = "XP: ${user.xp ?: 0}" // Jika xp null, tampilkan 0
                view.findViewById<TextView>(R.id.tv_xp).text = xpText

                // Tampilkan tahun join
                user.created_at?.let { instant ->
                    val joinYear = formatInstantToYear(instant)
                    view.findViewById<TextView>(R.id.tv_join_date).text = "Anggota sejak $joinYear"
                }

                // Tampilkan foto profil jika ada
                user.user_photo_profile?.let { imageUrl ->
                    Glide.with(this@ProfileFragment)
                        .load(imageUrl)
                        .placeholder(R.drawable.default_profile)
                        .circleCrop()
                        .into(profileImageView)
                }

                // Tombol edit username
                view.findViewById<ImageButton>(R.id.btn_edit_profile).setOnClickListener {
                    val editProfileFragment = EditProfileFragment().apply {
                        arguments = Bundle().apply {
                            putString("oldUsername", user.username)
                            putString("profilePhotoUrl", user.user_photo_profile)
                        }
                    }
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, editProfileFragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }


    private fun formatInstantToYear(instant: Instant): String { //nampilin tahun aja
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return localDateTime.year.toString()
    }
}