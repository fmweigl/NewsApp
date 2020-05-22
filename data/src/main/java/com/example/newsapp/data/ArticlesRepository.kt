package com.example.newsapp.data

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class ArticlesRepository(private val dataSource: IArticlesDataSource) {


    fun getArticles(keyword: String): Single<ArticlesResponse> =
        dataSource.getArticles(keyword).subscribeOn(Schedulers.io())

}