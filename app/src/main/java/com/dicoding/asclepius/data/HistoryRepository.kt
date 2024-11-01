package com.dicoding.asclepius.data

import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.data.local.room.HistoryDao

class HistoryRepository(private val dao: HistoryDao) {
    suspend fun insert(historyEntity: HistoryEntity){
        dao.insertHistory(historyEntity)
    }

    suspend fun delete(historyEntity: HistoryEntity){
        dao.delete(historyEntity)
    }

    fun getAllHistory(): LiveData<List<HistoryEntity>> {
        return dao.getAllHistory()
    }
}