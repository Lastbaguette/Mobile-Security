package com.example.myapplication

import android.util.Log

class BookRepository(private val bookDao: BookDao) {

    suspend fun insertBook(book: BookEntity) {
        try {
            bookDao.insertBook(book)
        } catch (e: Exception) {
            Log.e("BookRepository", "Error inserting book", e)
            throw e
        }
    }

    suspend fun getBookById(id: String): BookEntity? {
        return try {
            bookDao.getBookById(id)
        } catch (e: Exception) {
            Log.e("BookRepository", "Error loading book", e)
            null
        }
    }
}
