package com.example.newsapp.data

import com.example.newsapp.domain.Source
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.then
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.*

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
    fun `should get articles for keyword and map them to domain class`() {
        val page = 1
        val pageSize = 11
        val keyword = "keyword"
        val status = "ok"
        val totalResults = 22
        val name = "name"
        val source = SourceResponse(name)
        val author = "author"
        val title = "title"
        val description = "description"
        val url = "url"
        val urlToImage = "urlToImage"
        val publishedAt = mock<Date>()
        val content = "content"
        val article = ArticleResponse(
            source = source,
            author = author,
            title = title,
            description = description,
            url = url,
            urlToImage = urlToImage,
            publishedAt = publishedAt,
            content = content
        )
        val response = ArticlesResponse(
            status = status,
            totalResults = totalResults,
            articles = listOf(article)
        )

        given(dataSource.getArticles(keyword, page, pageSize)).willReturn(Single.just(response))

        val testObserver = tested.getArticles(keyword, page, pageSize).test()
        testObserver
            .assertNoErrors()
            .assertComplete()

        val articles = testObserver.values().first()
        assertEquals(status, articles.status)
        assertEquals(totalResults, articles.totalResults)
        val resultArticle = articles.articles.first()
        assertEquals(Source(name), resultArticle.source)
        assertEquals(author, resultArticle.author)
        assertEquals(title, resultArticle.title)
        assertEquals(description, resultArticle.description)
        assertEquals(url, resultArticle.url)
        assertEquals(urlToImage, resultArticle.urlToImage)
        assertEquals(publishedAt, resultArticle.publishedAt)
        assertEquals(content, resultArticle.content)

        then(dataSource).should().getArticles(keyword, page, pageSize)
    }

}