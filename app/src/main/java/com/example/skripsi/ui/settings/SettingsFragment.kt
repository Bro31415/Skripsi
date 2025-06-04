package com.example.skripsi.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.skripsi.MyApp
import com.example.skripsi.R
import com.example.skripsi.data.repository.UserRepository
import com.example.skripsi.ui.auth.SignInActivity
import com.example.skripsi.ui.auth.ForgotPasswordActivity
import kotlinx.coroutines.launch


class SettingsFragment : Fragment() {

    private lateinit var btnResetPassword: Button
    private lateinit var btnLogout: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        btnResetPassword = view.findViewById(R.id.btn_reset_password)
        btnLogout = view.findViewById(R.id.btn_logout)

        btnResetPassword.setOnClickListener {
            resetPassword()
        }

        btnLogout.setOnClickListener {
            logout()
        }

        return view
    }

    private fun resetPassword() {
        val intent = Intent(requireContext(), ForgotPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun logout() {
        lifecycleScope.launch {
            val success = UserRepository().logoutUser()
            if (success) {
                Toast.makeText(requireContext(), "Berhasil logout", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), SignInActivity::class.java)
                startActivity(intent)
                activity?.finishAffinity()
            } else {
                Toast.makeText(requireContext(), "Gagal logout", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
