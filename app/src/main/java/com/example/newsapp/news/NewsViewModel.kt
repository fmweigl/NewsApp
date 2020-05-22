package com.example.newsapp.news

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.newsapp.usecase.GetArticlesUseCase

class NewsViewModel(private val getArticlesUseCase: GetArticlesUseCase) : ViewModel() {

    fun onKeywordInput(text: String) {
        if (text.isNotBlank()) getArticlesForKeyword(text)
    }

    private fun getArticlesForKeyword(keyword: String) {
        getArticlesUseCase.getArticles(keyword)
            .subscribe({
                Log.d("yyy", "got $it")
            }, {
                Log.e("yyy", it.toString())
            })
    }

}