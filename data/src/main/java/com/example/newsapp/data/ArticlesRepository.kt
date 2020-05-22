package com.example.newsapp.data

class ArticlesRepository(private val dataSource: IArticlesDataSource) {


    fun getArticles(keyword: String) = dataSource.getArticles(keyword)

}