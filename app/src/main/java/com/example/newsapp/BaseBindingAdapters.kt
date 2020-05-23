package com.example.newsapp

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.news.ImageLoader

@BindingAdapter("bindImageUrl")
fun ImageView.bindImageUrl(url: String?) {
    if (url == null) {
        setImageDrawable(null)
    } else {
        ImageLoader.loadInto(url, this)
    }
}

@BindingAdapter("bindLastVisibleGridPosition")
fun RecyclerView.bindLastVisibleGridPosition(lastVisiblePositionListener: ILastVisiblePositionListener) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            (layoutManager as? GridLayoutManager)?.findLastVisibleItemPosition()?.let {
                lastVisiblePositionListener.onLastVisiblePosition(it)
            }
        }
    })
}

interface ILastVisiblePositionListener {

    fun onLastVisiblePosition(position: Int)

}