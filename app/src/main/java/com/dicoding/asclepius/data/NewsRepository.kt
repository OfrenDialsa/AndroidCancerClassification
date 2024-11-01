package com.dicoding.asclepius.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.data.remote.retrofit.ApiService
import com.dicoding.asclepius.BuildConfig


class NewsRepository private constructor(
    private val apiService: ApiService,
    ) {

    fun getHeadlineNews(): LiveData<Result<List<ArticlesItem>>> = liveData {
        emit(Result.Loading())
        try {
            val response = apiService.getNews(BuildConfig.API_KEY)
            if (response.status == "ok") {
                val articles = response.articles
                emit(Result.Success(articles))
            } else {
                emit(Result.Error("Failed to fetch news: ${response.status}"))
            }
        } catch (e: Exception) {
            Log.d("NewsRepository", "getHeadlineNews: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: NewsRepository? = null
        fun getInstance(
            apiService: ApiService,
        ): NewsRepository =
            instance ?: synchronized(this) {
                instance ?: NewsRepository(apiService)
            }.also { instance = it }
    }
}