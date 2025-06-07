// File: ui/AchievementsFragment.kt
package com.example.skripsi.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.skripsi.R
import com.example.skripsi.data.repository.UserRepository
import com.example.skripsi.ui.course.AchievementAdapter
import com.example.skripsi.viewmodel.AuthViewModel
import com.example.skripsi.viewmodel.factory.AuthViewModelFactory

class AchievementsFragment : Fragment() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var rvAchievements: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_achievements, container, false)
        val userRepository = UserRepository()
        authViewModel = ViewModelProvider(requireActivity(), AuthViewModelFactory(userRepository))[AuthViewModel::class.java]


        val toolbar: Toolbar = view.findViewById(R.id.toolbar_achievements)
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        rvAchievements = view.findViewById(R.id.rv_achievements)


        val userId = arguments?.getString(ARG_USER_ID)
        if (userId != null) {
            loadAllAchievements(userId)
        }

        return view
    }

    private fun loadAllAchievements(userId: String) {
        authViewModel.loadUserAchievements(userId) { achievements ->
            activity?.runOnUiThread {
                rvAchievements.adapter = AchievementAdapter(achievements)
            }
        }
    }

    companion object {
        private const val ARG_USER_ID = "user_id"

        fun newInstance(userId: String): AchievementsFragment {
            return AchievementsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USER_ID, userId)
                }
            }
        }
    }
}