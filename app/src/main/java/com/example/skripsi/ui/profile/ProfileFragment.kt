package com.example.skripsi.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

class ProfileFragment : Fragment() {

    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val userRepository = UserRepository()
        authViewModel = ViewModelProvider(this, AuthViewModelFactory(userRepository)).get(AuthViewModel::class.java)

        val currentUser = MyApp.supabase.auth.currentUserOrNull()
        val userId = currentUser?.id

        if (userId != null) {
            Log.d("ProfileFragment", "User ID: $userId") // Periksa userId
            authViewModel.getUserProfile(userId) { user ->
                if (user != null) {
                    Log.d("ProfileFragment", "User data retrieved: ${user.username}") // Periksa username
                    view.findViewById<TextView>(R.id.tv_username).text = user.username
                    // format dan tampilan tahun user
                    user.created_at?.let { instant ->
                        val joinYear = formatInstantToYear(instant)
                        view.findViewById<TextView>(R.id.tv_join_date).text = "Anggota sejak $joinYear"
                    }
                    // Tombol untuk mengarahkan ke EditUsernameFragment
                    view.findViewById<Button>(R.id.btn_edit_username).setOnClickListener {
                        val editUsernameFragment = EditUsernameFragment().apply {
                            arguments = Bundle().apply {
                                putString("oldUsername", user.username) // Kirim username lama ke EditUsernameFragment
                            }
                        }
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, editUsernameFragment)
                            .addToBackStack(null) // Tambahkan ke back stack agar bisa kembali ke ProfileFragment
                            .commit()
                    }
                } else {
                    Log.e("ProfileFragment", "User not found")
                }
            }
        } else {
            Log.e("ProfileFragment", "User not authenticated")
        }
        return view
    }

    private fun formatInstantToYear(instant: Instant): String { //nampilin tahun aja
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return localDateTime.year.toString()
    }
}