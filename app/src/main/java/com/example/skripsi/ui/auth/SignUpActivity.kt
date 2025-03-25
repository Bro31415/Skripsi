package com.example.skripsi.ui.auth

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import com.example.skripsi.R
import com.example.skripsi.utils.isEmailValid
import io.github.jan.supabase.createSupabaseClient
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.skripsi.data.repository.UserRepository
import com.example.skripsi.viewmodel.AuthViewModel
import com.example.skripsi.viewmodel.factory.AuthViewModelFactory

//import com.example.skripsi.utils.isEmailValid


class SignUpActivity : AppCompatActivity() {

    private var isPasswordVisible = false

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val etEmail = findViewById<EditText>(R.id.et_email)
        val etUsername = findViewById<EditText>(R.id.et_username)
        val etPassword = findViewById<EditText>(R.id.et_password)

        val btnSignUp = findViewById<Button>(R.id.btn_signup)
        val btnTogglePassword = findViewById<ImageButton>(R.id.btn_togglePassword)

        val userRepository = UserRepository()
        val authViewModel:AuthViewModel by viewModels {AuthViewModelFactory(userRepository)}

        // Validate User Input
//        if (email == null || email == ""){
//            Toast.makeText(this, "Email has to be filled", Toast.LENGTH_SHORT).show()
//        }
//
//        if (password == null || password == ""){
//            Toast.makeText(this, "Email has to be filled", Toast.LENGTH_SHORT).show()
//        }

        // Hash Password

        // Pass Hashed Password to AuthViewModel

        // Handle Response

        // Navigate to Login

        btnTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility(etPassword, btnTogglePassword)
        }

        btnSignUp.setOnClickListener{

            val email = etEmail.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            Log.d("Supabase Sign Up", "Email sent: '$email'")

            if (!isEmailValid(email)){
                Toast.makeText(this, "invalid email format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authViewModel.signUp(username, email, password) { success ->
                if (success) {
                    Toast.makeText(this, "Sign Up Successful!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Sign Up Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun togglePasswordVisibility(editText: EditText, imageButton: ImageButton) {
        if (isPasswordVisible) {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            imageButton.setImageResource(R.drawable.bold_eye)
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            imageButton.setImageResource(R.drawable.bold_eye_closed)
        }
        editText.setSelection(editText.text.length)

        editText.typeface = Typeface.create("sans-serif", Typeface.NORMAL)
    }

}