package com.dicoding.asclepius.data.remote.retrofit

import com.dicoding.asclepius.data.remote.response.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines?q=cancer&country=us&category=health")
    suspend fun getNews(@Query("apiKey") apiKey: String): NewsResponse
}