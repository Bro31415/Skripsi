package com.example.skripsi.ui.auth

import android.content.Intent
import androidx.credentials.CredentialManager
import android.credentials.GetCredentialException
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.lifecycleScope
import com.example.skripsi.MyApp
import com.example.skripsi.R
import com.example.skripsi.data.repository.UserRepository
import com.example.skripsi.ui.HomeActivity
import com.example.skripsi.utils.isEmailValid
import com.example.skripsi.viewmodel.AuthViewModel
import com.example.skripsi.viewmodel.factory.AuthViewModelFactory
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.material.appbar.MaterialToolbar
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import kotlinx.coroutines.launch

class SignInActivity : AppCompatActivity() {

    private val WEB_CLIENT_ID = "348152894853-55jbdoh97t96pt0p7r3j3jbg5418i2q8.apps.googleusercontent.com"

    private lateinit var credentialManager: CredentialManager

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_password)

        val btnSignIn = findViewById<Button>(R.id.btn_signin)
        val btnForgotPassword = findViewById<TextView>(R.id.btn_forgotpassword)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)

        val userRepository = UserRepository()
        val authViewModel:AuthViewModel by viewModels {AuthViewModelFactory(userRepository)}

        val btnSignInGoogle = findViewById<Button>(R.id.btn_signinGoogle)

        credentialManager = CredentialManager.create(this)

        toolbar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
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
                    finish()
                } else {
                    Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnSignInGoogle.setOnClickListener {
            signInWithGoogle()
        }

        btnForgotPassword.setOnClickListener{
            val intent = Intent(this, ForgotPasswordActivity::class.java)

            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun signInWithGoogle() {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(WEB_CLIENT_ID)
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = this@SignInActivity
                )

                val credential = result.credential
                var idToken: String? = null

                if (credential is GoogleIdTokenCredential) {
                    idToken = credential.idToken
                    Log.d("CredentialManager", "Handled as GoogleIdTokenCredential.")
                    signInWithSupabase(idToken)
                } else if (credential is androidx.credentials.CustomCredential) {
                    val tokenKey = "com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID_TOKEN"
                    idToken = credential.data.getString(tokenKey)
                    Log.d("CredentialManager", "Handled as CustomCredential.")
                }

                if (idToken != null) {
                    Log.d("CredentialManager", "Successfully acquired Google ID Token.")
                    signInWithSupabase(idToken)
                } else {
                    Log.e("CredentialManager", "Failed to get ID token from any credential type.")
                }

            } catch (e: GetCredentialException) {
                Log.e("CredentialManager", "No credential available or user canceled.", e)
                Toast.makeText(this@SignInActivity, "No Google accounts found on this device.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun signInWithSupabase(idToken: String) {
        lifecycleScope.launch {
            try {
                MyApp.supabase.auth.signInWith(Google, idToken)
                Log.i("SupabaseAuth", "Successfully signed in with Supabase")
                val intent = Intent(this@SignInActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e:Exception) {
                Log.e("SupabaseAuth", "Supabase sign in failed", e)
            }
        }
    }
}
