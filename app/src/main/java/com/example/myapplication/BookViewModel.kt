package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookViewModel(private val repository: BookRepository) : ViewModel() {
    private val _book = MutableStateFlow<BookEntity?>(null)
    val book: StateFlow<BookEntity?> = _book

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadBook(id: String) {
        viewModelScope.launch {
            try {
                val result = repository.getBookById(id)
                _book.value = result
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load book: ${e.message}"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
