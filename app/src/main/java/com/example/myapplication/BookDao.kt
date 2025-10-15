package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookDao {
    @Insert
    suspend fun insertBook(book: BookEntity)

    @Query("SELECT * FROM book_table WHERE id = :id")
    suspend fun getBookById(id: String): BookEntity?
}
