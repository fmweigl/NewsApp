package com.example.newsapp.news

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.domain.Articles
import com.example.newsapp.usecase.GetArticlesUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

class NewsViewModel(private val getArticlesUseCase: GetArticlesUseCase) : ViewModel() {

    private val _articles = MutableLiveData<Articles>()
    val articles: LiveData<Articles> = _articles

    fun onKeywordInput(text: String) {
        if (text.isNotBlank()) getArticlesForKeyword(text)
    }

    private fun getArticlesForKeyword(keyword: String) {
        getArticlesUseCase.getArticles(keyword)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("yyy", "got ${it.articles.size} for $keyword")
                _articles.value = it

            }, {
                Log.e("yyy", it.toString())
            })
    }

}