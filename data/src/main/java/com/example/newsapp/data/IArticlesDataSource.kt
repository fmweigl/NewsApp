package com.example.newsapp.data

import io.reactivex.rxjava3.core.Single

interface IArticlesDataSource {

    fun getArticles(keyword: String, page: Int, pageSize: Int): Single<ArticlesResponse>

}