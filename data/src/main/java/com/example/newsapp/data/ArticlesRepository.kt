package com.example.newsapp.data

import com.example.newsapp.domain.Article
import com.example.newsapp.domain.Articles
import com.example.newsapp.domain.Source
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class ArticlesRepository(private val dataSource: IArticlesDataSource) {

    fun getArticles(keyword: String, page: Int, pageSize: Int): Single<Articles> =
        dataSource.getArticles(keyword = keyword, page = page, pageSize = pageSize)
            .map { it.mapResponseToDomainModel(keyword, page) }
            .subscribeOn(Schedulers.io())

    private fun ArticlesResponse.mapResponseToDomainModel(keyword: String, page: Int) =
        Articles(
            status = status,
            totalResults = totalResults,
            articles = articles.map { it.mapResponseToDomainModel() },
            keyword = keyword,
            page = page
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