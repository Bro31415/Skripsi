package com.example.skripsi

import android.app.Application
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.ktor.client.*
import io.ktor.client.engine.android.*


class MyApp : Application() {

    companion object {
        lateinit var supabase: SupabaseClient
    }

    override fun onCreate() {
        super.onCreate()

        supabase = createSupabaseClient(
            supabaseUrl = "https://vgxzvsljfikfkmgqvmoz.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InZneHp2c2xqZmlrZmttZ3F2bW96Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzQ5Mjg5OTYsImV4cCI6MjA1MDUwNDk5Nn0.7PgpkS7ZmNWQ4xzlqllejlXCqJLXqPjRlQItI_Fjzdg"
        ) {
            install(Auth)
            install(Postgrest)
            install(Realtime)
        }
    }

}