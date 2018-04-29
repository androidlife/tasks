package com.tasks

import com.tasks.network.ApiManager
import org.junit.Assert.assertTrue
import org.junit.Test


class SimpleFetchTest {
    @Test
    fun fetchTaskFeed() {
        val taskFeed = ApiManager.apiService.getTasksFeed().blockingGet()
        assertTrue(taskFeed != null && taskFeed.feeds.isNotEmpty() && taskFeed.profileIds.isNotEmpty()
        && taskFeed.taskIds.isNotEmpty())
    }
}
