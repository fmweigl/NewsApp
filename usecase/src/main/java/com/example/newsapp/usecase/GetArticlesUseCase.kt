package com.example.newsapp.usecase

import com.example.newsapp.data.ArticlesRepository
import com.example.newsapp.domain.Articles
import io.reactivex.rxjava3.core.Single

class GetArticlesUseCase(private val repository: ArticlesRepository) {

    fun loadArticlesForKeyword(keyword: String, page: Int, pageSize: Int): Single<Articles> =
        repository.getArticles(keyword = keyword, page = page, pageSize = pageSize)
}