package com.dicoding.asclepius.view

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.data.HistoryRepository
import com.dicoding.asclepius.data.NewsRepository
import com.dicoding.asclepius.di.Injection
import com.dicoding.asclepius.view.history.HistoryViewModel
import com.dicoding.asclepius.view.news.NewsViewModel
import com.dicoding.asclepius.view.result.HistoryInsViewModel

class ViewModelFactory private constructor(
    private val eventRepository: NewsRepository,
    private val historyRepository: HistoryRepository,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(NewsViewModel::class.java) -> {
                NewsViewModel(eventRepository) as T
            }
            modelClass.isAssignableFrom(HistoryInsViewModel::class.java) -> {
                HistoryInsViewModel(historyRepository) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(historyRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val applicationContext = when (context) {
                    is Application -> context
                    is FragmentActivity -> context.applicationContext
                    else -> context.applicationContext
                }
                INSTANCE ?: run { // Get application context directly
                    val newsRepository = Injection.provideRepository()
                    val historyRepository = Injection.provideHistoryRepository(applicationContext)
                    ViewModelFactory(newsRepository, historyRepository).also {
                        INSTANCE = it
                    }
                }
            }
        }
    }
}