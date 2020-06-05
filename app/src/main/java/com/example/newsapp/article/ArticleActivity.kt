package com.example.newsapp.article

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.example.newsapp.ExtraKeys
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityArticleBinding

class ArticleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityArticleBinding>(this, R.layout.activity_article)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val url: String = intent.getStringExtra(ExtraKeys.EXTRA_ARTICLE_URL)
            ?: throw IllegalArgumentException("Need article url")

        binding.wvArticle.webViewClient = WebViewClient()
        binding.wvArticle.loadUrl(url)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
