package com.example.skripsi.data.repository

import UserAchievement
import android.util.Log
import com.example.skripsi.MyApp.Companion.supabase
import com.example.skripsi.data.model.User
import com.example.skripsi.data.model.UserQuizAttempt
import com.example.skripsi.data.model.UserXp
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserProgressRepository(
    private val supabaseClient: SupabaseClient
) {

    suspend fun insertUserQuizAttempt(
        attempt: UserQuizAttempt
    ): Boolean {
        return try {
                supabase.from("user_quiz_attempt")
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
            val currentXp = supabase.from("users")
                .select(columns = Columns.list("xp")) {
                    filter {
                        eq("id", userId)
                    }
                }.decodeSingleOrNull<UserXp>()

            val newXp = (currentXp?.xp ?: 0) + xpToAdd

            supabase.from("users")
                .update(mapOf("xp" to newXp)) {
                    filter {
                        eq("id", userId)
                    }
                }
            true
        } catch (e: Exception) {
            Log.e("UserProgressRepo", "Failed to update user xp", e)
            false
        }
    }

    suspend fun unlockAchievement(userId: String, achievementKey: String): Boolean {
        return try {
            val unlockedAt = kotlinx.datetime.Clock.System.now().toString()

            supabase.from("user_achievement")
                .insert(
                    mapOf(
                        "user_id" to userId,
                        "achievement_key" to achievementKey,
                        "unlocked_at" to unlockedAt
                    )
                )
            true
        } catch (e: Exception) {
            Log.e("UserProgressRepo", "Failed to unlock achievement", e)
            false
        }
    }

    suspend fun isAchievementUnlocked(userId: String, achievementKey: String): Boolean {
        return try {
            val result = supabase.from("user_achievement")
                .select {
                    filter {
                        eq("user_id", userId)
                        eq("achievement_key", achievementKey)
                    }
                    limit(1)
                }
                .decodeList<UserAchievement>()
            result.isNotEmpty()
        } catch (e: Exception) {
            Log.e("UserProgressRepo", "Failed to check achievement", e)
            false
        }
    }

}