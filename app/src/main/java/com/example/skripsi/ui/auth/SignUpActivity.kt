package com.example.skripsi.ui.auth

import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import android.graphics.Color
import android.view.View
import com.example.skripsi.R
import com.example.skripsi.utils.isEmailValid
import io.github.jan.supabase.createSupabaseClient
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.skripsi.data.repository.UserRepository
import com.example.skripsi.viewmodel.AuthViewModel
import com.example.skripsi.viewmodel.factory.AuthViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

class SignUpActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val etEmail = findViewById<EditText>(R.id.et_email)
        val etUsername = findViewById<EditText>(R.id.et_username)
        val etPassword = findViewById<EditText>(R.id.et_password)

        val btnSignUp = findViewById<Button>(R.id.btn_signup)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)

        val userRepository = UserRepository()
        val authViewModel:AuthViewModel by viewModels {AuthViewModelFactory(userRepository)}

        toolbar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnSignUp.setOnClickListener{

            val email = etEmail.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // TODO: validation goes here

            if (!isEmailValid(email)){
                Toast.makeText(this, "invalid email format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnSignUp.isEnabled = false
            btnSignUp.text = "Mendaftar..."

            authViewModel.signUp(username, email, password) { success ->
                runOnUiThread {
                    btnSignUp.isEnabled = true
                    btnSignUp.text = "Daftar"

                    if (success) {

                        showInfoDialog(
                            iconResId = R.drawable.asset_check,
                            title = "Pendaftaran Berhasil!",
                            subtitle = "Akun Anda telah berhasil dibuat. Silakan login untuk melanjutkan.",
                            buttonText = "Lanjut",
                            onButtonClick = {
                                val intent = Intent(this, SignInActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            }
                        )
                    } else {
                        showInfoDialog(
                            iconResId = R.drawable.asset_warning,
                            title = "Pendaftaran Gagal",
                            subtitle = "Email ini mungkin sudah terdaftar atau terjadi kesalahan pada sistem.",
                            buttonText = "OK"
                        )
                    }
                }
            }
        }

    }

    private fun showInfoDialog(
        iconResId: Int,
        title: String,
        subtitle: String,
        buttonText: String? = null,
        onButtonClick: (() -> Unit)? = null
    ) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_info, null)
        val dialogIcon = dialogView.findViewById<ImageView>(R.id.iv_dialog_icon)
        val dialogTitle = dialogView.findViewById<TextView>(R.id.tv_dialog_title)
        val dialogSubtitle = dialogView.findViewById<TextView>(R.id.tv_dialog_subtitle)
        val dialogButton = dialogView.findViewById<MaterialButton>(R.id.btn_dialog_action)

        dialogIcon.setImageResource(iconResId)
        dialogTitle.text = title
        dialogSubtitle.text = subtitle

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        if (buttonText != null) {
            dialogButton.visibility = View.VISIBLE
            dialogButton.text = buttonText
            dialogButton.setOnClickListener {
                dialog.dismiss()
                onButtonClick?.invoke()
            }
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

}