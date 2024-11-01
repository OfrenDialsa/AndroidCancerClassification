package com.dicoding.asclepius.view.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.HistoryRepository
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: HistoryRepository): ViewModel() {

    val history: LiveData<List<HistoryEntity>> = repository.getAllHistory()

    fun deleteHistory(historyEntity: HistoryEntity) {
        viewModelScope.launch {
            repository.delete(historyEntity)
        }
    }
}