package com.example.skripsi.data.repository

import com.example.skripsi.MyApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import io.github.jan.supabase.postgrest.from
import android.util.Log
import android.util.Patterns
import com.example.skripsi.data.model.User
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import io.ktor.util.reflect.instanceOf
import java.lang.reflect.Array.set


class UserRepository {

    suspend fun signUpUser(
        email: String,
        password: String
    ): Boolean {
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

    suspend fun signInUser(
        email: String,
        password: String
    ): Boolean {
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

    suspend fun createUserProfile(
        username: String
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val currentUser = MyApp.supabase.auth.currentUserOrNull()

                val userId = currentUser?.id
                val userEmail = currentUser?.email
                val createdAt = currentUser?.createdAt

                if (userId == null || userEmail == null) {
                    Log.e("UserProfile", "User not authenticated or missing information")
                    return@withContext false
                }

                val createUser = User(
                    id = userId,
                    email = userEmail,
                    username = username,
                    xp = 0,
                    created_at = createdAt
                )

                MyApp.supabase.postgrest.from("users").insert(createUser)
                true
            } catch (e: Exception) {
                Log.e("UserProfile", "Profile creation failed: ${e.message}")
                false
            }
        }
    }

    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                MyApp.supabase.auth.resetPasswordForEmail(email)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //tambahan wil (untuk ngambil user yg login) [19 mar 25]
    suspend fun getUserProfile(userId: String): User? {
        return withContext(Dispatchers.IO) {
            try {
                val user = MyApp.supabase.postgrest.from("users").select {
                    filter {
                        eq("id", userId)
                    }
                }.decodeSingleOrNull<User>()

                // Tambahkan log untuk memeriksa data user
                Log.d("UserProfile", "User data: $user")

                user
            } catch (e: Exception) {
                Log.e("UserProfile", "Failed to fetch user profile: ${e.message}")
                null
            }
        }
    }

    // update username
    suspend fun updateUsername(userId: String, newUsername: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // Update username di tabel users
                MyApp.supabase.postgrest.from("users").update({
                    set("username", newUsername)
                }) {
                    filter {
                        eq("id", userId)
                    }
                }
                true
            } catch (e: Exception) {
                Log.e("UserProfile", "Failed to update username: ${e.message}")
                false
            }
        }
    }

    suspend fun getTopUsers(limit: Int = 15): List<User> {
        return withContext(Dispatchers.IO) {
            try {
                val users = MyApp.supabase.postgrest.from("users")
                    .select()
                    .decodeList<User>()

                users.filter { user -> user.xp != null }
                    .sortedByDescending { user -> user.xp }
                    .take(limit)
            } catch (e: Exception) {
                Log.e("UserRepository", "Failed to get top users: ${e.message}")
                emptyList()
            }
        }
    }

    suspend fun logoutUser(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                MyApp.supabase.auth.signOut()
                Log.d("UserRepository", "Logout successful")
                true
            } catch (e: Exception) {
                Log.e("UserRepository", "Logout failed: ${e.message}")
                false
            }
        }
    }
}