package com.tasks.model


data class FeedItem(val taskId: Int, val profileId: Int, var text: String, var createdAt: String, var event: String) {
    lateinit var profile: Profile
    lateinit var task: Task
}

data class Feeds(val feedItems: List<FeedItem>, val tasks: Map<Task, List<Int>>, val profiles: Map<Profile, List<Int>>)




