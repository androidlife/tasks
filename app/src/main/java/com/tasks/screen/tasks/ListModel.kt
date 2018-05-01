package com.tasks.screen.tasks

import com.tasks.model.Error
import com.tasks.model.FeedItem
import com.tasks.network.TaskService
import com.tasks.screen.tasks.transformer.FeedsTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 *  Model contract implementation class
 *  Here we do various api calls chaining
 *  and for that we require
 *  @see TaskService
 *  and
 *  @see FeedsTransformer
 *  @param taskService
 *  is for api calls
 *  @param feedsTransformer
 *  is to chain various API calls
 *  It's main task is to get List<FeedItem> from various API
 */
class ListModel(private val taskService: TaskService, private val feedsTransformer: FeedsTransformer) : ListContract.Model {

    private var cancel: Boolean = false
    private var apiCallback: Disposable? = null

    override fun fetchFeeds(listener: ListContract.OnFeedsFetchListener) {
        apiCallback = taskService.getTasksFeed().map { feeds ->
            feedsTransformer.combineFeedsWithRemoteCalls(feeds, taskService)
        }.flatMap { triple ->
            feedsTransformer.zipProfilesAndTasksCall(triple)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ feeds: List<FeedItem> -> onSuccess(feeds, listener) }, { _ ->
                    onFailure(Error(Error.ErrorType.Fetch), listener)
                })
    }

    /**
     * If model is cancelled, we no longer need any
     * ongoing API calls
     */
    override fun cancel(cancel: Boolean) {
        this.cancel = cancel
        if (this.cancel)
            apiCallback?.dispose()
    }


    private fun onSuccess(feeds: List<FeedItem>, listener: ListContract.OnFeedsFetchListener) {
        if (cancel)
            return
        listener.onFetchSuccess(feeds)
    }

    private fun onFailure(error: Error, listener: ListContract.OnFeedsFetchListener) {
        if (cancel)
            return
        listener.onFetchFailure(error)
    }

}
