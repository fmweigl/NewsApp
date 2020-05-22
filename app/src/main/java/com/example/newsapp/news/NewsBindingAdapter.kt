package com.example.newsapp.news

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.domain.Article

@BindingAdapter("bindArticles")
fun RecyclerView.bindArticles(articles: List<Article>?) {
    articles?.let {
        (adapter as? ArticlesAdapter)?.submitList(articles)
    }

}