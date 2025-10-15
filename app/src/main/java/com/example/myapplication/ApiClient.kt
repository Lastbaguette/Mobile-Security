package com.example.myapplication

object ApiClient {
    val apiService: OpenLibraryApi by lazy {
        NetworkClient.createRetrofit().create(OpenLibraryApi::class.java)
    }
}
