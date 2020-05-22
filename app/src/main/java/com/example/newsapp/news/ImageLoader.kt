package com.example.newsapp.news

import android.widget.ImageView
import com.bumptech.glide.Glide

object ImageLoader {

    fun loadInto(loadUrl: String, intoImageView: ImageView) {
        Glide.with(intoImageView).load(loadUrl).into(intoImageView)
    }

}