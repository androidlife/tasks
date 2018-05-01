package com.tasks.screen.tasks

import com.tasks.model.Error
import com.tasks.model.FeedItem

/**
 * ListContract.Presenter implementation class
 * which coordinates between View and Model
 * @see ListContract
 * As per the ViewState
 * @see ListContract.View.ViewState
 * this class will either delegate the model to fetch data
 * or show the necessary view state
 */
class ListPresenter(val view: ListContract.View, val model: ListContract.Model) : ListContract.Presenter {


    //Doing necessary actions as per the View State
    //By default View is Empty
    private fun initView(viewState: ListContract.View.ViewState) {
        when (viewState) {
            ListContract.View.ViewState.Empty -> fetchListing()
            ListContract.View.ViewState.Error -> view.showError(true)
            ListContract.View.ViewState.Loaded -> {
                //do nothing
            }
        }
    }

    /**
     * Fetching the data from model in case
     * view is in Empty state, the empty check here is
     * reduntant
     */
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

    // Setting the view state to error
    private fun setViewError(errorMsg: String) {
        view.showProgress(false)
        view.setViewState(ListContract.View.ViewState.Error)
        view.showError(true)
        view.showInfo(errorMsg)
    }

    //ListContract.Presenter implementation

    //retry logic
    override fun retry() {
        view.setViewState(ListContract.View.ViewState.Empty)
        fetchListing()
    }

    //initialization state for this class whether to start or stop this class
    override fun start(start: Boolean) {
        when (start) {
            true -> initView(view.getViewState())
            false -> model.cancel(true)
        }
    }

}
