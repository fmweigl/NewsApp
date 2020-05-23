package com.example.newsapp.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.newsapp.R
import com.example.newsapp.databinding.ItemArticleBigBinding
import com.example.newsapp.databinding.ItemArticleSmallBinding
import com.example.newsapp.domain.Article

class ArticlesAdapter(private val viewTypeLookup: IArticleViewTypeLookup) :
    ListAdapter<Article, AbsArticleViewHolder>(DIFFUTIL_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbsArticleViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_SMALL -> DataBindingUtil.inflate<ItemArticleSmallBinding>(
                layoutInflater,
                R.layout.item_article_small,
                parent,
                false
            ).let {
                SmallArticleViewHolder(it)
            }
            VIEW_TYPE_BIG -> DataBindingUtil.inflate<ItemArticleBigBinding>(
                layoutInflater,
                R.layout.item_article_big,
                parent,
                false
            ).let {
                BigArticleViewHolder(it)
            }

            else -> throw IllegalArgumentException("Don't know how to handle view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: AbsArticleViewHolder, position: Int) {
        holder.bindArticle(getItem(position))
    }

    override fun getItemViewType(position: Int): Int = viewTypeLookup.viewTypeForPosition(position)

    companion object {

        private val DIFFUTIL_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }

        }

        private const val VIEW_TYPE_SMALL = 0
        private const val VIEW_TYPE_BIG = 1

    }

}