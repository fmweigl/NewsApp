package com.example.newsapp.news

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.ItemArticleBigBinding
import com.example.newsapp.databinding.ItemArticleSmallBinding
import com.example.newsapp.domain.Article

abstract class AbsArticleViewHolder(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    abstract fun bindArticle(article: Article)

}

class SmallArticleViewHolder(private val binding: ItemArticleSmallBinding) :
    AbsArticleViewHolder(binding) {

    override fun bindArticle(article: Article) {
        binding.article = article
    }
}

class BigArticleViewHolder(private val binding: ItemArticleBigBinding) :
    AbsArticleViewHolder(binding) {

    override fun bindArticle(article: Article) {
        binding.article = article
    }

}