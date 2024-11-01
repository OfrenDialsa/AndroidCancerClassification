package com.dicoding.asclepius.view.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.NewsRepository
import com.dicoding.asclepius.data.Result
import com.dicoding.asclepius.data.remote.response.ArticlesItem

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    // This will expose LiveData<Result<List<ArticlesItem>>>
    val headlineNews: LiveData<Result<List<ArticlesItem>>> = newsRepository.getHeadlineNews()

}