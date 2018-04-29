package com.tasks.model

import io.reactivex.Single


data class FeedItem(val taskId: Int, val profileId: Int, var text: String, var createdAt: String, var event: String) {
    lateinit var profile: Profile
    lateinit var task: Task
}

data class Feeds(val feedItems: List<FeedItem>, val taskIds: Set<Int>, val profileIds: Set<Int>)
data class FeedsObservable(val feedItems: List<FeedItem>, val tasks: Single<List<Task>>,
                           val profiles: ArrayList<Single<Profile>>)



