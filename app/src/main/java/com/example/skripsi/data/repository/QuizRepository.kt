package com.example.skripsi.data.repository

import com.example.skripsi.MyApp.Companion.supabase
import com.example.skripsi.data.model.Question
import com.example.skripsi.data.model.Quiz
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns


class QuizRepository {

    suspend fun getQuizId(
        chapterId: Long,
        quizType: String
    ): Long {
        val quiz = supabase.from("quiz")
            .select(columns = Columns.list("id")) {
                filter {
                    eq("chapter_id", chapterId)
                    eq("quiz_type", quizType)
                }
            }.decodeSingle<Quiz>()

        return quiz.id
    }

    suspend fun getQuizQuestions(
        quizId: Long
    ): List<Question> {
        val question = supabase.from("question")
            .select() {
                filter {
                    eq("quiz_id", quizId)
                }
            }.decodeList<Question>()
        return question
    }
}