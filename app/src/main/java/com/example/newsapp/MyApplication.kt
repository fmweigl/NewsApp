package com.example.newsapp

import android.app.Application
import com.example.newsapp.data.IArticlesDataSource
import com.example.newsapp.framework.IArticlesRetrofitDataSource
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val NEWSAPI_BASE_URL = "https://newsapi.org"

class MyApplication : Application() {

    private val module = module {
        single {
            OkHttpClient.Builder()
                .addNetworkInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
                .build()
        }
        single<IArticlesDataSource> {
            Retrofit.Builder()
                .client(get())
                .baseUrl(NEWSAPI_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(IArticlesRetrofitDataSource::class.java)
        }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(listOf(module))
        }
    }

}