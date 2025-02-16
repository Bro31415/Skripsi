package com.example.skripsi.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.skripsi.R
import com.example.skripsi.utils.isEmailValid
import io.github.jan.supabase.createSupabaseClient
import at.favre.lib.crypto.bcrypt.BCrypt
//import com.example.skripsi.utils.isEmailValid


class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val etEmail = findViewById<EditText>(R.id.et_email)
        val etUsername = findViewById<EditText>(R.id.et_username)
        val etPassword = findViewById<EditText>(R.id.et_password)

        val btnSignUp = findViewById<Button>(R.id.btn_signup)

        // Retrieve User Input
        val email = etEmail.text.toString().trim()
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // Validate User Input
        if (email == null || email == ""){
            Toast.makeText(this, "Email has to be filled", Toast.LENGTH_SHORT).show()
        }

        if (password == null || password == ""){
            Toast.makeText(this, "Email has to be filled", Toast.LENGTH_SHORT).show()
        }

        // Hash Password

        val hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray())

        // Pass Hashed Password to AuthViewModel

        // Handle Response

        // Navigate to Login



    }

}