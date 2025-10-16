package com.example.myapplication

import retrofit2.http.GET
import retrofit2.http.Query

data class OpenLibrarySearchResponse(
    val numFound: Int,
    val docs: List<BookDoc>?
)

interface OpenLibraryApi {
    @GET("search.json")
    suspend fun searchBooks(
        @Query("q") query: String
    ): OpenLibrarySearchResponse
}
