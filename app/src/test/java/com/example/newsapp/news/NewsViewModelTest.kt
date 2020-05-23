package com.example.newsapp.news

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.newsapp.domain.Article
import com.example.newsapp.domain.Articles
import com.example.newsapp.usecase.GetArticlesUseCase
import com.nhaarman.mockitokotlin2.*
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class NewsViewModelTest {

    @Rule
    @JvmField
    var rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getArticlesUseCase: GetArticlesUseCase

    @Mock
    private lateinit var navigator: INewsNavigator

    @InjectMocks
    private lateinit var tested: NewsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        tested.navigator = navigator
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun tearDown() = RxAndroidPlugins.setInitMainThreadSchedulerHandler { null }

    @Test
    fun `should not load articles if keyword input is blank`() {
        tested.onKeywordInput(" ")

        then(getArticlesUseCase).should(never()).loadArticlesForKeyword(any(), any(), any())
    }

    @Test
    fun `should load and set and replace articles when keyword entered`() {
        val keyword1 = "keyword1"
        val keyword2 = "keyword2"
        val articles1 = Articles(
            status = "",
            totalResults = 0,
            articles = emptyList(),
            keyword = keyword1,
            page = 1
        )
        val articles2 = Articles(
            status = "",
            totalResults = 0,
            articles = emptyList(),
            keyword = keyword2,
            page = 1
        )
        given(getArticlesUseCase.loadArticlesForKeyword(keyword1, 1, NewsViewModel.PAGE_SIZE))
            .willReturn(Single.just(articles1))
        given(getArticlesUseCase.loadArticlesForKeyword(keyword2, 1, NewsViewModel.PAGE_SIZE))
            .willReturn(Single.just(articles2))

        tested.onKeywordInput(keyword1)

        then(getArticlesUseCase).should()
            .loadArticlesForKeyword(keyword1, 1, NewsViewModel.PAGE_SIZE)
        assertEquals(articles1, tested.articles.value)

        tested.onKeywordInput(keyword2)

        then(getArticlesUseCase).should()
            .loadArticlesForKeyword(keyword2, 1, NewsViewModel.PAGE_SIZE)
        assertEquals(articles2, tested.articles.value)
    }

    @Test
    fun `should load second page of articles when scrolled to position near end of list and combine with previous articles`() {
        val keyword = "keyword"
        val firstPageArticles = (0 until NewsViewModel.PAGE_SIZE).map { mock<Article>() }
        val articles1 = Articles(
            status = "",
            totalResults = 100,
            articles = firstPageArticles,
            keyword = keyword,
            page = 1
        )
        val secondPageArticles = (0 until NewsViewModel.PAGE_SIZE).map { mock<Article>() }
        val articles2 = Articles(
            status = "",
            totalResults = 100,
            articles = secondPageArticles,
            keyword = keyword,
            page = 2
        )
        given(getArticlesUseCase.loadArticlesForKeyword(keyword, 1, NewsViewModel.PAGE_SIZE))
            .willReturn(Single.just(articles1))
        given(getArticlesUseCase.loadArticlesForKeyword(keyword, 2, NewsViewModel.PAGE_SIZE))
            .willReturn(Single.just(articles2))

        tested.onKeywordInput(keyword)

        tested.onScrolledToPosition(17) // page size - threshold

        then(getArticlesUseCase).should()
            .loadArticlesForKeyword(keyword, 2, NewsViewModel.PAGE_SIZE)
        assertEquals(firstPageArticles.plus(secondPageArticles), tested.articles.value?.articles)
    }

    @Test
    fun `should not load next page if scrolling position is below threshold`() {
        val keyword = "keyword"
        val firstPageArticles = (0 until NewsViewModel.PAGE_SIZE).map { mock<Article>() }
        val articles1 = Articles(
            status = "",
            totalResults = 100,
            articles = firstPageArticles,
            keyword = keyword,
            page = 1
        )
        given(getArticlesUseCase.loadArticlesForKeyword(keyword, 1, NewsViewModel.PAGE_SIZE))
            .willReturn(Single.just(articles1))

        tested.onKeywordInput(keyword)

        tested.onScrolledToPosition(16)

        then(getArticlesUseCase).should(never()).loadArticlesForKeyword(any(), eq(2), any())
    }

    @Test
    fun `should not try to load next page while still loading`() {
        val keyword = "keyword"
        val firstPageArticles = (0 until NewsViewModel.PAGE_SIZE).map { mock<Article>() }
        val articles1 = Articles(
            status = "",
            totalResults = 100,
            articles = firstPageArticles,
            keyword = keyword,
            page = 1
        )
        given(getArticlesUseCase.loadArticlesForKeyword(keyword, 1, NewsViewModel.PAGE_SIZE))
            .willReturn(Single.just(articles1))
        given(getArticlesUseCase.loadArticlesForKeyword(keyword, 2, NewsViewModel.PAGE_SIZE))
            .willReturn(Single.never())

        tested.onKeywordInput(keyword)

        tested.onScrolledToPosition(18)
        tested.onScrolledToPosition(19)
        tested.onScrolledToPosition(20)

        then(getArticlesUseCase).should(times(1))
            .loadArticlesForKeyword(keyword, 2, NewsViewModel.PAGE_SIZE)
    }

    @Test
    fun `should not try to load next page if all articles have been loaded`() {
        val keyword = "keyword"
        val articles1 = Articles(
            status = "",
            totalResults = 1,
            articles = listOf(mock()),
            keyword = keyword,
            page = 1
        )
        given(getArticlesUseCase.loadArticlesForKeyword(keyword, 1, NewsViewModel.PAGE_SIZE))
            .willReturn(Single.just(articles1))

        tested.onScrolledToPosition(0)

        then(getArticlesUseCase).should(never()).loadArticlesForKeyword(any(), eq(2), any())
    }

    @Test
    fun `should show error snackbar when loading articles fails`() {
        val throwable: Throwable = mock()
        given(
            getArticlesUseCase.loadArticlesForKeyword(
                any(),
                any(),
                any()
            )
        ).willReturn(Single.error(throwable))

        tested.onKeywordInput("keyword")

        then(navigator).should().showErrorSnackbar(throwable)
    }

    @Test
    fun `should display article when it is clicked`() {
        val url = "url"
        val article: Article = mock {
            on { this.url } doReturn url
        }

        tested.onArticleClicked(article)

        then(navigator).should().showArticle(url)
    }
}