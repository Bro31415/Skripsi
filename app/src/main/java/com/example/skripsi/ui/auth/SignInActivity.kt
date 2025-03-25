package com.example.skripsi.ui.auth

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import com.example.skripsi.R
import com.example.skripsi.data.repository.UserRepository
import com.example.skripsi.ui.HomeActivity
import com.example.skripsi.utils.isEmailValid
import com.example.skripsi.viewmodel.AuthViewModel
import com.example.skripsi.viewmodel.factory.AuthViewModelFactory

class SignInActivity : AppCompatActivity() {

    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_password)

        val btnSignIn = findViewById<Button>(R.id.btn_signin)
        val btnTogglePassword = findViewById<ImageButton>(R.id.btn_togglePassword)
        val btnForgotPassword = findViewById<Button>(R.id.btn_forgotpassword)

        val userRepository = UserRepository()
        val authViewModel:AuthViewModel by viewModels {AuthViewModelFactory(userRepository)}

        btnTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility(etPassword, btnTogglePassword)
        }

        btnSignIn.setOnClickListener{
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val intent = Intent(this, HomeActivity::class.java)

            if (!isEmailValid(email)){
                Toast.makeText(this, "invalid email format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authViewModel.signIn(email, password) { success ->
                if (success) {
                    Toast.makeText(this, "Sign In Successful!", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnForgotPassword.setOnClickListener{
            val intent = Intent(this, ForgotPasswordActivity::class.java)

            startActivity(intent)
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
