package com.tasks.screen.tasks

import com.tasks.model.Error
import com.tasks.model.FeedItem

//Overall contract used by Model, View and Presenter
// for our TaskListActivity or screen
interface ListContract {

    interface View {
        enum class ViewState {
            Empty, Error, Loaded
        }

        fun getViewState(): ViewState
        fun setViewState(viewViewState: ViewState)
        fun showError(show: Boolean)
        fun showProgress(show: Boolean)
        fun showInfo(infoMsg: String)
        fun isConnectedToNetwork(): Boolean
        fun setData(listing: List<FeedItem>)
        fun getFetchedData(): List<FeedItem>
    }

    interface Presenter {
        fun retry()
        fun start(start: Boolean)
    }

    interface Model {
        fun fetchFeeds(listener: OnFeedsFetchListener)
        fun cancel(cancel: Boolean)
    }

    interface OnFeedsFetchListener {
        fun onFetchSuccess(feeds: List<FeedItem>)
        fun onFetchFailure(error: Error)
    }
}
