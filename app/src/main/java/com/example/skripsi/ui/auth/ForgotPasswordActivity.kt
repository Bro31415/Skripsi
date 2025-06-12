package com.example.skripsi.ui.auth

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.skripsi.R
import com.example.skripsi.data.repository.UserRepository
import com.example.skripsi.viewmodel.AuthViewModel
import com.example.skripsi.viewmodel.factory.AuthViewModelFactory
import com.google.android.material.button.MaterialButton
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

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        val userRepository = UserRepository()
        val authViewModel: AuthViewModel by viewModels { AuthViewModelFactory(userRepository) }

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnReset.setOnClickListener{
            val email = etEmail.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Mohon masukkan email anda yang terdaftar pada sistem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnReset.isEnabled = false
            btnReset.text = "Mengirim..."
            authViewModel.sendPasswordResetEmail(email)
        }

        authViewModel.resetPasswordResult.observe(this, Observer { result ->

            btnReset.isEnabled = true
            btnReset.text = "Reset Password"

            result?.let {
                if (it.isSuccess) {
                    showInfoDialog(
                        iconResId = R.drawable.asset_check,
                        title = "Link reset sandi \n" +
                                "terkirim ke email Anda!",
                        subtitle = "Silakan periksa email Anda untuk melanjutkan proses reset kata sandi.",
                        buttonText = "Lanjut"
                    ) {
                        finish()
                    }
                } else {
                    showInfoDialog(
                        iconResId = R.drawable.asset_warning,
                        title = "Gagal Mengirim",
                        subtitle = "Email tidak ditemukan atau terjadi kesalahan. Silakan coba lagi.",
                        buttonText = "OK"
                    )
                }
            }
        })
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
