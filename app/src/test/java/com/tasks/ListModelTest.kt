package com.tasks

import com.tasks.model.Error
import com.tasks.model.FeedItem
import com.tasks.provider.Injection
import com.tasks.screen.tasks.ListContract
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


// Testing the working mechanism of ListModel implementation
// with assumption that there is network and result is always feeds
class ListModelTest {

    var feedsMain: List<FeedItem> = ArrayList<FeedItem>()
    private val latch: CountDownLatch = CountDownLatch(1)

    companion object {
        @BeforeClass
        @JvmStatic
        fun replaceAndroidSchedulers() {
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> Schedulers.trampoline() }
        }
    }

    @Before
    fun clearFeeds() {
        feedsMain = ArrayList()
    }

    @Test
    fun testListModel() {
        val listModel = Injection.getListModel()
        listModel.fetchFeeds(object : ListContract.OnFeedsFetchListener {
            override fun onFetchSuccess(feeds: List<FeedItem>) {
                feedsMain = feeds
            }

            override fun onFetchFailure(error: Error) {
            }

        })
        latch.await(4, TimeUnit.SECONDS)
        Assert.assertTrue(feedsMain.isNotEmpty())
    }
}
