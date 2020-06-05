package com.example.newsapp.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.domain.Article
import com.example.newsapp.domain.Articles
import com.example.newsapp.usecase.GetArticlesUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable

class NewsViewModel(private val getArticlesUseCase: GetArticlesUseCase) : ViewModel(),
    IArticleClickListener {

    private val _articles = MutableLiveData<Articles>()
    val articles: LiveData<Articles> = _articles

    var navigator: INewsNavigator? = null

    private var loading = false
    private var disposable: Disposable? = null

    // TODO debounce input to avoid needless backend calls
    fun onKeywordInput(text: String) {
        if (text.isNotBlank()) {
            loadArticlesForKeyword(text, FIRST_PAGE)
        }
    }

    override fun onArticleClicked(article: Article) {
        navigator?.showArticle(article.url)
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
        disposable?.dispose()
        disposable = getArticlesUseCase.loadArticlesForKeyword(
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
                navigator?.showErrorSnackbar(it)
                it.printStackTrace()
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

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    companion object {
        const val PAGE_SIZE = 21
        const val FIRST_PAGE = 1
        const val LOAD_MORE_THRESHOLD = 4
    }

}