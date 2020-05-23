package com.example.newsapp.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityNewsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsActivity : AppCompatActivity() {

    private val viewModel: NewsViewModel by viewModel()
    private val spanCount: Int by lazy {
        resources.getInteger(R.integer.news_span_count)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityNewsBinding>(this, R.layout.activity_news)
            .also {
                it.lifecycleOwner = this
                it.viewModel = viewModel
                it.rvArticles.adapter = ArticlesAdapter(viewModel)
                it.rvArticles.layoutManager =
                    GridLayoutManager(this, spanCount).apply {
                        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                            override fun getSpanSize(position: Int): Int {
                                return when (viewModel.viewTypeForPosition(position)) {
                                    IArticleViewTypeLookup.VIEWTYPE_BIG -> spanCount
                                    IArticleViewTypeLookup.VIEWTYPE_SMALL -> 1
                                    else -> 0
                                }
                            }

                        }
                    }

                // TODO create decorator and remove margins from item_article_small.xml / item_article_big.xml
            }
    }
}
