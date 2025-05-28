package com.example.skripsi.data.repository

import android.util.Log
import com.example.skripsi.MyApp.Companion.supabase
import com.example.skripsi.data.model.User
import com.example.skripsi.data.model.UserQuizAttempt
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserProgressRepository {

    suspend fun insertUserQuizAttempt(
        attempt: UserQuizAttempt
    ): Boolean {
        return try {
                val insert = supabase.from("user_quiz_attempt")
                    .insert(attempt)
                true
        } catch (e:Exception) {
                Log.e("UserProgressRepo", "Failed to insert quiz attempt to database", e)
                false
        }
    }

    suspend fun updateUserXP(
        userId: String,
        xpToAdd: Int
    ): Boolean {
        return try {
            val userId = supabase.auth.currentUserOrNull()?.id
                ?: throw IllegalStateException("This user is not logged in")

            val currentXp = supabase.from("users")
                .select(columns = Columns.list("xp")) {
                    filter {
                        eq("id", userId)
                    }
                }.decodeSingle<User>()

            val newXp = currentXp.xp?.plus(xpToAdd)

            val updateXp = supabase.from("users")
                .update(mapOf("xp" to newXp)) {
                    filter {
                        eq("id", userId)
                    }
                }.decodeSingle<User>()

            updateXp != null

            true
        } catch (e: Exception) {
            Log.e("UserProgressRepo", "Failed to update user xp", e)
            false
        }
    }

}