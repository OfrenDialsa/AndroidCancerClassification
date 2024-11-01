package com.dicoding.asclepius.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Primary key with auto-increment
    val imageUri: String,                            // URI of the image as a string
    val result: String,                              // Result of the image analysis
    val timestamp: Long                            // Timestamp of when the entry was created
)