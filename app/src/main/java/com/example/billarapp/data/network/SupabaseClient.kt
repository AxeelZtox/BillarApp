package com.example.billarapp.data.network

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

fun supabaseBillar(): SupabaseClient {
    return createSupabaseClient(
        supabaseUrl = "https://wzrkocvzumwitfzqupfd.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Ind6cmtvY3Z6dW13aXRmenF1cGZkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzE3ODMyMzYsImV4cCI6MjA0NzM1OTIzNn0.1jM2tWTRo4oyMPyRkKVbke2wslipINDzxh31rqpyjDo"
    ) {
        install(Postgrest)
    }
}
