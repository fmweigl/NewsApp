package com.example.newsapp.news

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.domain.Articles
import com.example.newsapp.usecase.GetArticlesUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

class NewsViewModel(private val getArticlesUseCase: GetArticlesUseCase) : ViewModel(),
    IArticleViewTypeLookup {

    private val _articles = MutableLiveData<Articles>()
    val articles: LiveData<Articles> = _articles

    private var loading = false

    fun onKeywordInput(text: String) {
        if (text.isNotBlank()) {
            loadArticlesForKeyword(text, FIRST_PAGE)
        }
    }

    fun onScrolledToPosition(position: Int) {
        if (loading) return
        val loadedArticlesSize = _articles.value?.articles?.size ?: return
        val totalResults = articles.value?.totalResults ?: return
        if (loadedArticlesSize == totalResults) return
        if (loadedArticlesSize - position <= LOAD_MORE_THRESHOLD) {
            articles.value?.let {
                loadArticlesForKeyword(it.keyword, it.page + 1)
            }
        }
    }

    private fun loadArticlesForKeyword(keyword: String, page: Int) {
        getArticlesUseCase.loadArticlesForKeyword(
            keyword = keyword,
            page = page,
            pageSize = PAGE_SIZE
        )
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loading = true }
            .doOnEvent { _, _ -> loading = false }
            .subscribe({
                onArticlesLoaded(it)
            }, {
                Log.e("yyy", it.toString())
            })
    }

    private fun onArticlesLoaded(loadedArticles: Articles) {

        if (articles.value?.keyword == loadedArticles.keyword) {
            val combinedArticles =
                articles.value?.articles?.plus(loadedArticles.articles)
                    ?: loadedArticles.articles
            _articles.value = articles.value?.copy(
                page = loadedArticles.page,
                articles = combinedArticles
            )
        } else {
            _articles.value = loadedArticles
        }
    }

    override fun viewTypeForPosition(position: Int): Int {
        return if (position % BIG_ARTICLE_INTERVAL == 0) IArticleViewTypeLookup.VIEWTYPE_BIG else IArticleViewTypeLookup.VIEWTYPE_SMALL
    }

    companion object {
        const val PAGE_SIZE = 21
        const val FIRST_PAGE = 1
        const val LOAD_MORE_THRESHOLD = 4
        const val BIG_ARTICLE_INTERVAL = 7
    }

}