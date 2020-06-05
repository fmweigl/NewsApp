package com.example.newsapp.data

import com.google.gson.annotations.SerializedName
import java.util.*

data class ArticlesResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int,
    @SerializedName("articles")
    val articles: List<ArticleResponse>
)

data class ArticleResponse(
    @SerializedName("source")
    val source: SourceResponse,
    @SerializedName("author")
    val author: String?,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("url")
    val url: String,
    @SerializedName("urlToImage")
    val urlToImage: String?,
    @SerializedName("publishedAt")
    val publishedAt: Date,
    @SerializedName("content")
    val content: String?
)

data class SourceResponse(
    @SerializedName("name")
    val name: String
)
