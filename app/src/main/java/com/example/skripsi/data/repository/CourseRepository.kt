package com.example.skripsi.data.repository

import android.util.Log
import com.example.skripsi.MyApp.Companion.supabase
import com.example.skripsi.data.model.Chapter
import com.example.skripsi.data.model.ChapterWithQuizzes
import com.example.skripsi.data.model.Question
import com.example.skripsi.data.model.Quiz
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

class CourseRepository (private val supabase: SupabaseClient) {

    suspend fun getChaptersWithQuizzes(): List<ChapterWithQuizzes> {
        return try {
            val chapters = supabase.from("chapter")
                .select(columns = Columns.list("id", "name", "created_at"))
                .decodeList<Chapter>().also {
                    Log.d("CourseRepo", "Chapters loaded: ${it.size}")
                }

            val quizzes = supabase.from("quiz")
                .select(columns = Columns.list("id", "chapter_id", "quiz_type", "created_at"))
                .decodeList<Quiz>().also {
                    Log.d("CourseRepo", "Quizzes loaded: ${it.size}")
                }

            chapters.map { chapter ->
                val chapterQuizzes = quizzes.filter { it.chapterId == chapter.id }
                ChapterWithQuizzes(chapter, chapterQuizzes)
            }
        } catch (e: Exception) {
            Log.e("CourseRepo", "Failed to fetch chapters/quizzes", e)
            emptyList()
        }
    }

    suspend fun getQuestionsByQuizId(quizId: Long): List<Question> {
        return try {
            supabase.from("question")
                .select(columns = Columns.list(
                    "id",
                    "quiz_id",
                    "question_text",
                    "question_type",
                    "answer",
                    "options",
                    "xp",
                    "created_at")) {
                    filter {
                        eq("quiz_id", quizId)
                    }
                }.decodeList<Question>()
                .also { Log.d("CourseRepo", "Questions loaded for quiz $quizId: ${it.size}") }
        } catch (e: Exception) {
            Log.e("CourseRepo", "Failed to fetch questions for quiz $quizId", e)
            emptyList()
        }
    }

}