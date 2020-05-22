package com.example.newsapp.usecase

import com.example.newsapp.data.ArticlesRepository
import com.example.newsapp.data.ArticlesResponse
import com.example.newsapp.domain.Articles
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.then
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class GetArticlesUseCaseTest {

    @Mock
    private lateinit var repository: ArticlesRepository

    @InjectMocks
    private lateinit var tested: GetArticlesUseCase

    @Before
    fun setUp() = MockitoAnnotations.initMocks(this)

    @Test
    fun `should get articles for keyword`() {
        val keyword = "keyword"
        val articles: Articles = mock()

        given(repository.getArticles(keyword)).willReturn(Single.just(articles))

        tested.getArticles(keyword)
            .test()
            .assertValue(articles)
            .assertNoErrors()
            .assertComplete()
        then(repository).should().getArticles(keyword)
    }

}