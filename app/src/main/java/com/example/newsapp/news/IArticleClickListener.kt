package com.example.newsapp.news

import com.example.newsapp.domain.Article

interface IArticleClickListener {

    fun onArticleClicked(article: Article)

}