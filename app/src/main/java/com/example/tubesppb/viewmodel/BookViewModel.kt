package com.example.tubesppb.viewmodel

import androidx.lifecycle.*
import com.example.tubesppb.database.Book
import com.example.tubesppb.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class BookViewModel(private val repository: BookRepository): ViewModel() {
    val allBooks: LiveData<List<Book>> = repository.allBooks.asLiveData()
    val someBooks: LiveData<List<Book>> = repository.someBooks.asLiveData()

    fun insert(book: Book) = viewModelScope.launch {
        repository.insert(book)
    }

    fun certainBooks(title: String): LiveData<List<Book>> {
        return repository.getCertainBook(title).asLiveData()
    }

    fun delBook(title: String) {
        repository.delBook(title)
    }

    fun check(title: String): Boolean {
        return repository.bookCheck(title)
    }
}

class BookViewModelFactory(private val repository: BookRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}