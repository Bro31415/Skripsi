package com.example.skripsi.data.repository

import com.example.skripsi.MyApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import io.github.jan.supabase.postgrest.from

class UserRepository {

    suspend fun signUpUser(email: String, hashedPassword: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                MyApp.supabase.from("users").insert(
                    mapOf(
                        "email" to email,
                        "hashedPassword" to hashedPassword
                    )
                )
                true
            } catch (e: Exception) {
                false
            }
        }
    }

}