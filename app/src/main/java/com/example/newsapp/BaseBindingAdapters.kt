package com.example.newsapp

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.newsapp.news.ImageLoader

@BindingAdapter("bindImageUrl")
fun ImageView.bindImageUrl(url: String?) {
    if (url == null) {
        setImageDrawable(null)
    } else {
        ImageLoader.loadInto(url, this)
    }
}