package com.example.newsapp.news

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.ExtraKeys
import com.example.newsapp.R
import com.example.newsapp.article.ArticleActivity
import com.example.newsapp.databinding.ActivityNewsBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsActivity : AppCompatActivity(), INewsNavigator {

    private val viewModel: NewsViewModel by viewModel()
    private val spanCount: Int by lazy {
        resources.getInteger(R.integer.news_span_count)
    }
    private var binding: ActivityNewsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityNewsBinding>(this, R.layout.activity_news)
            .also {
                it.lifecycleOwner = this
                it.viewModel = viewModel
                it.rvArticles.adapter = ArticlesAdapter(viewModel, viewModel)
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
        viewModel.navigator = this
    }

    override fun showArticle(url: String) {
        Intent(this, ArticleActivity::class.java).apply {
            putExtra(ExtraKeys.EXTRA_ARTICLE_URL, url)
            startActivity(this)
        }
    }

    override fun showErrorSnackbar(throwable: Throwable) {
        binding?.root?.let {
            Snackbar.make(it, throwable.toString(), Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.navigator = null
    }
}
