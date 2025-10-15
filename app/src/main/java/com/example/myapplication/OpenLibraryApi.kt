package com.example.myapplication

import retrofit2.http.GET
import retrofit2.http.Query

data class OpenLibrarySearchResponse(
    val numFound: Int,
    val docs: List<BookDoc>?
)

data class BookDoc(
    val title: String?,
    val author_name: List<String>?,
    val first_publish_year: Int?
)

interface OpenLibraryApi {
    @GET("search.json")
    suspend fun searchBooks(
        @Query("q") query: String
    ): OpenLibrarySearchResponse
}
