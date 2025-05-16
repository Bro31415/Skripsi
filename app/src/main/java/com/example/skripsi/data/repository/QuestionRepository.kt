package com.example.skripsi.data.repository

import android.util.Log // Import Log dari Android SDK
import com.example.skripsi.MyApp
import com.example.skripsi.data.model.Question
import com.example.skripsi.data.model.QuestionType
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order

class QuestionRepository {
    private val postgrest = MyApp.supabase.postgrest
    private val TAG = "QuestionRepository" // Tag untuk logging

    suspend fun getQuestionsByType(
        quizId: Int,
        questionType: QuestionType
    ): List<Question> {
        return try {
            val questions = postgrest["question"]
                .select {
                    filter {
                        eq("quiz_id", quizId)
                        eq("question_type", questionType)
                    }
                    order("created_at", Order.ASCENDING)
                }
                .decodeList<Question>()

            // Log jumlah pertanyaan yang berhasil diambil
            Log.d(TAG, "Fetched ${questions.size} ${questionType.name} questions")
            questions
        } catch (e: Exception) {
            // Log error dengan stack trace
            Log.e(TAG, "Failed to fetch ${questionType.name} questions: ${e.message}", e)
            emptyList()
        }
    }

    // Contoh fungsi khusus untuk multiple choice
    suspend fun getMultipleChoiceQuestions(quizId: Int): List<Question> {
        return getQuestionsByType(quizId, QuestionType.multiple_choice)
    }
}