package com.example.newsapp.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.newsapp.R
import com.example.newsapp.data.IArticlesDataSource
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.android.ext.android.inject

class NewsActivity : AppCompatActivity() {

    private val source: IArticlesDataSource by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        source.getArticles("bitcoin")
            .subscribeOn(Schedulers.io())
            .subscribe({
                Log.d("yyy", "got $it")
            }, {
                Log.e("yyy", it.toString())
            })
    }
}
