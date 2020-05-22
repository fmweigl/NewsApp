package com.example.newsapp.data

import com.example.newsapp.domain.Article
import com.example.newsapp.domain.Articles
import com.example.newsapp.domain.Source
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class ArticlesRepository(private val dataSource: IArticlesDataSource) {

    fun getArticles(keyword: String): Single<Articles> =
        dataSource.getArticles(keyword)
            .map { it.mapResponseToDomainModel() }
            .subscribeOn(Schedulers.io())

    private fun ArticlesResponse.mapResponseToDomainModel() =
        Articles(
            status = status,
            totalResults = totalResults,
            articles = articles.map { it.mapResponseToDomainModel() }
        )

    private fun ArticleResponse.mapResponseToDomainModel() =
        Article(
            title = title,
            author = author,
            content = content,
            description = description,
            url = url,
            urlToImage = urlToImage,
            publishedAt = publishedAt,
            source = source.mapResponseToDomainModel()
        )

    private fun SourceResponse.mapResponseToDomainModel() =
        Source(
            name = name
        )

}