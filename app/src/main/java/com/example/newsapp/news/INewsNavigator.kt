package com.example.newsapp.news

interface INewsNavigator {

    fun showArticle(url: String)

    fun showErrorSnackbar(throwable: Throwable)

}