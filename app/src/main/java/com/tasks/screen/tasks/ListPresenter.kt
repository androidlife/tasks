package com.tasks.screen.tasks

import com.tasks.model.Error
import com.tasks.model.FeedItem

class ListPresenter(val view: ListContract.View, val model: ListContract.Model) : ListContract.Presenter {


    private fun initView(viewState: ListContract.View.ViewState) {
        when (viewState) {
            ListContract.View.ViewState.Empty -> fetchListing()
            ListContract.View.ViewState.Error -> view.showError(true)
            ListContract.View.ViewState.Loaded -> {
                //do nothing
            }
        }
    }

    private fun fetchListing() {
        if (view.getViewState() == ListContract.View.ViewState.Empty) {
            if (!view.isConnectedToNetwork()) {
                setViewError("No internet connection")
                return
            }
            model.cancel(false)
            view.showError(false)
            view.showProgress(true)
            model.fetchFeeds(object : ListContract.OnFeedsFetchListener {
                override fun onFetchSuccess(feeds: List<FeedItem>) {
                    view.showProgress(false)
                    view.setData(feeds)
                    view.setViewState(ListContract.View.ViewState.Loaded)
                }

                override fun onFetchFailure(error: Error) {
                    setViewError("Error fetching tasks")
                }
            })
        }
    }

    private fun setViewError(errorMsg: String) {
        view.showProgress(false)
        view.setViewState(ListContract.View.ViewState.Error)
        view.showError(true)
        view.showInfo(errorMsg)
    }

    //ListContract.Presenter implementation
    override fun retry() {
        view.setViewState(ListContract.View.ViewState.Empty)
        fetchListing()
    }

    override fun start(start: Boolean) {
        when (start) {
            true -> initView(view.getViewState())
            false -> model.cancel(true)
        }
    }

}
