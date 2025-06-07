package com.example.skripsi.ui.auth

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.skripsi.R
import com.example.skripsi.data.repository.UserRepository
import com.example.skripsi.viewmodel.AuthViewModel
import com.example.skripsi.viewmodel.factory.AuthViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password)



        var etEmail = findViewById<EditText>(R.id.et_forgotPassword)

        var btnReset = findViewById<Button>(R.id.btn_resetPassword)

        val userRepository = UserRepository()
        val authViewModel: AuthViewModel by viewModels { AuthViewModelFactory(userRepository) }

        btnReset.setOnClickListener{
            val email = etEmail.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Mohon masukkan email anda yang terdaftar pada sistem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authViewModel.sendPasswordResetEmail(email)
        }

        authViewModel.resetPasswordResult.observe(this, Observer { result ->
            result?.let {
                if (it.isSuccess) {

                } else {

                }
            }
        })
    }
}
