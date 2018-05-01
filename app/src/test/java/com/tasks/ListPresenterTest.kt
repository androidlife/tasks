package com.tasks

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.tasks.model.Error
import com.tasks.model.FeedItem
import com.tasks.screen.tasks.ListContract
import com.tasks.screen.tasks.ListPresenter
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when` as _when

class ListPresenterTest {
    @Mock
    lateinit var listModel: ListContract.Model
    @Mock
    lateinit var view: ListContract.View

    lateinit var listPresenter: ListContract.Presenter

    @Before
    fun initalize() {
        MockitoAnnotations.initMocks(this)
        listPresenter = ListPresenter(view, listModel)
    }

    @Test
    fun fetchSuccessTest() {
        val feedItem1 = Mockito.mock(FeedItem::class.java)
        val feedItem2 = Mockito.mock(FeedItem::class.java)
        val list: List<FeedItem> = listOf(feedItem1, feedItem2)

        _when(view.isConnectedToNetwork()).thenReturn(true)
        _when(view.getViewState()).thenReturn(ListContract.View.ViewState.Empty)

        listPresenter.start(true)

        argumentCaptor<ListContract.OnFeedsFetchListener>().apply {
            Mockito.verify(listModel).fetchFeeds(capture())
            firstValue.onFetchSuccess(list)
        }
        Mockito.verify(view).setData(list)
        Mockito.verify(view).setViewState(ListContract.View.ViewState.Loaded)
    }

    @Test
    fun verifyNetworkFailureTest() {
        _when(view.isConnectedToNetwork()).thenReturn(false)
        _when(view.getViewState()).thenReturn(ListContract.View.ViewState.Empty)
        listPresenter.start(true)
        Mockito.verify(view).showInfo(ArgumentMatchers.anyString())
        Mockito.verify(view).setViewState(ListContract.View.ViewState.Error)
    }

    @Test
    fun fetchFailureTest() {
        _when(view.isConnectedToNetwork()).thenReturn(true)
        _when(view.getViewState()).thenReturn(ListContract.View.ViewState.Empty)

        listPresenter.start(true)

        argumentCaptor<ListContract.OnFeedsFetchListener>().apply {
            Mockito.verify(listModel).fetchFeeds(capture())
            firstValue.onFetchFailure(com.tasks.model.Error(Error.ErrorType.Fetch))
        }
        Mockito.verify(view).showInfo(ArgumentMatchers.anyString())
        Mockito.verify(view).setViewState(ListContract.View.ViewState.Error)
    }

    @Test
    fun retrySuccessTest() {
        val feedItem = Mockito.mock(FeedItem::class.java)
        val list: List<FeedItem> = listOf(feedItem)
        _when(view.isConnectedToNetwork()).thenReturn(true)
        _when(view.getViewState()).thenReturn(ListContract.View.ViewState.Empty)

        listPresenter.retry()
        argumentCaptor<ListContract.OnFeedsFetchListener>().apply {
            Mockito.verify(listModel).fetchFeeds(capture())
            firstValue.onFetchSuccess(list)
        }
        Mockito.verify(view).setData(list)
        Mockito.verify(view).setViewState(ListContract.View.ViewState.Loaded)
    }


}
