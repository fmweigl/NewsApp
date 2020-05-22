package com.example.newsapp.data

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.then
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ArticlesRepositoryTest {

    @Mock
    private lateinit var dataSource: IArticlesDataSource

    @InjectMocks
    private lateinit var tested: ArticlesRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun tearDown() = RxJavaPlugins.setIoSchedulerHandler { null }

    @Test
    fun `should get articles for keyword`() {
        val keyword = "keyword"
        val response: ArticlesResponse = mock()

        given(dataSource.getArticles(keyword)).willReturn(Single.just(response))

        tested.getArticles(keyword)
            .test()
            .assertValue(response)
            .assertNoErrors()
            .assertComplete()
        then(dataSource).should().getArticles(keyword)
    }

}