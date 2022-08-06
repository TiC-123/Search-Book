package com.example.tubesppb.viewmodel

import androidx.lifecycle.*
import com.example.tubesppb.database.History
import com.example.tubesppb.repository.HistoryRepository
import kotlinx.coroutines.launch

class HistoryViewModel(private val historyRepository: HistoryRepository): ViewModel() {
    val allHistory: LiveData<List<History>> = historyRepository.historyData.asLiveData()

    fun insert(history: History) = viewModelScope.launch {
        historyRepository.insert(history)
    }

    fun delHistory() {
        historyRepository.delHistory()
    }
}

class HistoryViewModelFactory(private val historyRepository: HistoryRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(historyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}