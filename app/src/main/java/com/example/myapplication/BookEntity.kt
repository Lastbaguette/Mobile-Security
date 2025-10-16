package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_table")
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val year: String?,
    val filePath: String
)
