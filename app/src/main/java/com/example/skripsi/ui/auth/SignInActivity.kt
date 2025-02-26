package com.example.skripsi.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import com.example.skripsi.R
import com.example.skripsi.data.repository.UserRepository
import com.example.skripsi.viewmodel.AuthViewModel
import com.example.skripsi.viewmodel.factory.AuthViewModelFactory

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_password)

        val btnSignIn = findViewById<Button>(R.id.btn_signin)

        val userRepository = UserRepository()
        val authViewModel:AuthViewModel by viewModels {AuthViewModelFactory(userRepository)}
    }
}