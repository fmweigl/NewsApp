package com.example.newsapp.framework

import com.example.newsapp.data.ArticlesResponse
import com.example.newsapp.data.IArticlesDataSource
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface IArticlesRetrofitDataSource : IArticlesDataSource {

    @GET("/v2/everything?apiKey=4757a92b89cb4f8bac034f1e76c44919") // TODO put api key in appropriate place
    override fun getArticles(
        @Query("q") keyword: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Single<ArticlesResponse>

}