package com.example.skripsi.ui.settings

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.skripsi.MyApp
import com.example.skripsi.R
import com.example.skripsi.data.repository.UserRepository
import com.example.skripsi.ui.auth.SignInActivity
import com.example.skripsi.ui.auth.ForgotPasswordActivity
import com.example.skripsi.ui.dialog.CustomDialogFragment
import com.example.skripsi.viewmodel.AuthViewModel
import com.example.skripsi.viewmodel.factory.AuthViewModelFactory
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch


class SettingsFragment : Fragment() {

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(UserRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val btnResetPassword = view.findViewById<Button>(R.id.btn_reset_password)
        val btnLogout = view.findViewById<Button>(R.id.btn_logout)
        val btnResetProgress = view.findViewById<Button>(R.id.btn_reset_progress)

        btnResetPassword.setOnClickListener {
            val intent = Intent(requireContext(), ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        btnResetProgress.setOnClickListener{
            showResetProgressConfirmation()
        }

        btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.logoutResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                val intent = Intent(requireContext(), SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                showErrorDialog(
                    iconResId = R.drawable.asset_warning,
                    title = "Logout Gagal",
                    subtitle = "Terjadi kesalahan. Silakan coba lagi.",
                    buttonText = "OK"
                )
            }
        }

        // observer
        authViewModel.resetProgressResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                showErrorDialog(
                    iconResId = R.drawable.asset_check,
                    title = "Progres Direset",
                    subtitle = "Semua progres Anda telah berhasil dihapus.",
                    buttonText = "OK"
                )

            } else {
                showErrorDialog(
                    iconResId = R.drawable.asset_warning,
                    title = "Gagal Mereset",
                    subtitle = "Terjadi kesalahan, silakan coba lagi.",
                    buttonText = "OK"
                )
            }
        }
    }

    private fun showLogoutConfirmation() {
        val dialog = CustomDialogFragment.newInstance(
            iconResId = R.drawable.asset_warning,
            title = "Anda yakin ingin keluar dari aplikasi?",
            subtitle = "Sesi Anda akan berakhir dan Anda \n" +
                    "perlu masuk kembali untuk melanjutkan.",
            positiveButtonText = "Keluar",
            negativeButtonText = "Batal",
            listener = object : CustomDialogFragment.ConfirmationListener {
                override fun onConfirm() {
                    authViewModel.logoutUser()
                }

                override fun onCancel() {
                    Log.d("SettingsFragment", "Logout Cancelled")
                }
            }
        )
        dialog.show(parentFragmentManager, "LogoutConfirmationDialog")
    }

    private fun showResetProgressConfirmation() {
        val dialog = CustomDialogFragment.newInstance(
            iconResId = R.drawable.asset_warning,
            title = "Apakah Anda yakin ingin mereset progres Anda?",
            subtitle = "Progres yang sudah direset tidak dapat dipulihkan.",
            positiveButtonText = "Ya",
            negativeButtonText = "Tidak",
            listener = object : CustomDialogFragment.ConfirmationListener {
                override fun onConfirm() {
                    authViewModel.resetUserProgress()
                    Log.d("SettingsFragment", "User confirmed progress reset.")
                }

                override fun onCancel() {
                    Log.d("SettingsFragment", "Progress reset cancelled")
                }
            }
        )
        dialog.show(parentFragmentManager, "ResetProgressConfirmationDialog")
    }

    private fun showErrorDialog(
        iconResId: Int,
        title: String,
        subtitle: String,
        buttonText: String? = null,
        onButtonClick: (() -> Unit)? = null
    ){
        val dialogView = layoutInflater.inflate(R.layout.dialog_info, null)
        val dialogIcon = dialogView.findViewById<ImageView>(R.id.iv_dialog_icon)
        val dialogTitle = dialogView.findViewById<TextView>(R.id.tv_dialog_title)
        val dialogSubtitle = dialogView.findViewById<TextView>(R.id.tv_dialog_subtitle)
        val dialogButton = dialogView.findViewById<MaterialButton>(R.id.btn_dialog_action)

        dialogIcon.setImageResource(iconResId)
        dialogTitle.text = title
        dialogSubtitle.text = subtitle

        val dialog = AlertDialog.Builder(requireContext())
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
