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

class ArticlesAdapter(
    private val articleClickListener: IArticleClickListener
) :
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
                it.listener = articleClickListener
                SmallArticleViewHolder(it)
            }
            VIEW_TYPE_BIG -> DataBindingUtil.inflate<ItemArticleBigBinding>(
                layoutInflater,
                R.layout.item_article_big,
                parent,
                false
            ).let {
                it.listener = articleClickListener
                BigArticleViewHolder(it)
            }

            else -> throw IllegalArgumentException("Don't know how to handle view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: AbsArticleViewHolder, position: Int) {
        holder.bindArticle(getItem(position))
    }

    override fun getItemViewType(position: Int): Int =
        if (position % BIG_ARTICLE_INTERVAL == 0) VIEW_TYPE_BIG else VIEW_TYPE_SMALL

    companion object {

        private val DIFFUTIL_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }

        }

        const val VIEW_TYPE_SMALL = 0
        const val VIEW_TYPE_BIG = 1
        private const val BIG_ARTICLE_INTERVAL = 7

    }

}