package com.example.newsapp.news

interface IArticleViewTypeLookup {

    fun viewTypeForPosition(position: Int): Int

    companion object {
        const val VIEWTYPE_SMALL = 0
        const val VIEWTYPE_BIG = 1
    }

}