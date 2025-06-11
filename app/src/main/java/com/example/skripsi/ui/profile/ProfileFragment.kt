package com.example.skripsi.ui

import ProfileAchievementsAdapter
import android.graphics.Typeface
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
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private lateinit var achievementsRecyclerView: RecyclerView
    private lateinit var achievementsAdapter: ProfileAchievementsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileImageView = view.findViewById(R.id.iv_profile)
        val userRepository = UserRepository()
        authViewModel = ViewModelProvider(this, AuthViewModelFactory(userRepository)).get(AuthViewModel::class.java)

        setupRecyclerView(view)

        val userId = MyApp.supabase.auth.currentUserOrNull()?.id
        if (userId != null) {
            loadUserProfile(userId, view)
            authViewModel.loadStudyStreak(userId)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.studyStreak.collect { streak ->
                if (streak != null) {
                    view.findViewById<TextView>(R.id.tv_streak_value).text = streak.toString()
                }
            }
        }
    }

    private fun setupRecyclerView(view: View) {
        achievementsRecyclerView = view.findViewById(R.id.achievementsRecyclerView)
        achievementsAdapter = ProfileAchievementsAdapter()
        achievementsRecyclerView.adapter = achievementsAdapter
        achievementsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun loadUserProfile(userId: String, view: View) {
        Log.d("ProfileFragment", "User ID: $userId")
        authViewModel.getUserProfile(userId) { user ->
            if (user != null) {
                view.findViewById<TextView>(R.id.tv_username).text = user.username
                view.findViewById<TextView>(R.id.tv_xp).text = "[${user.xp ?: 0} XP]"

                user.user_photo_profile?.let { imageUrl ->
                    Glide.with(this@ProfileFragment)
                        .load(imageUrl)
                        .placeholder(R.drawable.default_profile)
                        .circleCrop()
                        .into(profileImageView)
                }

                user.created_at?.let { instant ->
                    val joinYear = formatInstantToYear(instant)
                    view.findViewById<TextView>(R.id.tv_join_date_value).text = joinYear
                }

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
                loadUserAchievements(userId, view)
            }
        }
    }

    private fun loadUserAchievements(userId: String, view: View) {
        val seeAllTextView: TextView = view.findViewById(R.id.tv_see_all_achievements)

        authViewModel.loadUserAchievements(userId) { achievements ->
            activity?.runOnUiThread {
                if (achievements.isNotEmpty()) {
                    val previewCount = 3
                    achievementsAdapter.submitList(achievements.take(previewCount))

                    if (achievements.size > previewCount) {
                        seeAllTextView.visibility = View.VISIBLE
                        seeAllTextView.setOnClickListener {
                            navigateToAchievementsFragment(userId)
                        }
                    } else {
                        seeAllTextView.visibility = View.GONE
                    }
                } else {
                    view.findViewById<TextView>(R.id.tv_achievements_title).visibility = View.GONE
                    achievementsRecyclerView.visibility = View.GONE
                    seeAllTextView.visibility = View.GONE
                }
            }
        }
    }

    private fun navigateToAchievementsFragment(userId: String) {
        val achievementsFragment = AchievementsFragment.newInstance(userId)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, achievementsFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun formatInstantToYear(instant: Instant): String { //nampilin tahun aja
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return localDateTime.year.toString()
    }
}