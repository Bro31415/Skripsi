package com.example.skripsi.data.repository

import android.util.Log
import com.example.skripsi.data.model.Dictionary
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

class DictionaryRepository(private val supabase: SupabaseClient) {

    suspend fun getDictionaryByChapterId(chapterId: Long): List<Dictionary> {
        return try {
            supabase.from("kamus")
                .select(columns = Columns.list(
                    "id",
                    "chapter_id",
                    "word",
                    "definition",
                    "example",
                    "translation",
                    "created_at"
                )) {
                    filter { eq("chapter_id", chapterId) }
                }
                .decodeList<Dictionary>()
                .also { Log.d("DictionaryRepo", "Loaded ${it.size} entries for chapter $chapterId") }
        } catch (e: Exception) {
            Log.e("DictionaryRepo", "Failed to fetch dictionary for chapter $chapterId", e)
            emptyList()
        }
    }
}
