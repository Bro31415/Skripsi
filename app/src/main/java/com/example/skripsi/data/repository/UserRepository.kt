package com.example.skripsi.data.repository

import com.example.skripsi.MyApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import io.github.jan.supabase.postgrest.from
import android.util.Log
import android.util.Patterns
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

class UserRepository {


    suspend fun signUpUser(email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                MyApp.supabase.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                }
                Log.d("SupabaseSignUp", "Sign up successful for $email")
                true
            } catch (e: Exception) {
                Log.d("SupabaseSignUp", "Sign up failed: ${e.message}", e)
                false
            }
        }
    }

    suspend fun signInUser(email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                MyApp.supabase.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }
                Log.d("SupabaseSignIn", "Sign in successful for $email")
                true
            } catch (e: Exception) {
                Log.d("SupabaseSignUp", "Sign up failed: ${e.message}", e)
                false
            }
        }
    }
}