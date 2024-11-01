package com.dicoding.asclepius.view.result

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.HistoryRepository
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import kotlinx.coroutines.launch

class HistoryInsViewModel(val repository: HistoryRepository) : ViewModel() {

    fun insert(historyEntity: HistoryEntity) = viewModelScope.launch {
        Log.d("HistoryInsViewModel", "Inserting: $historyEntity")
        try {
            repository.insert(historyEntity)
            Log.d("HistoryInsViewModel", "Insert successful")
        } catch (e: Exception) {
            Log.e("HistoryInsViewModelFavoriteEventViewModel", "Error inserting favorite event", e)
        }
    }
}