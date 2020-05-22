package com.example.newsapp.usecase

import com.example.newsapp.data.ArticlesRepository

class GetArticlesUseCase(private val repository: ArticlesRepository) {

    fun getArticles(keyword: String) = repository.getArticles(keyword)

}