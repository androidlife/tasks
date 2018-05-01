package com.tasks.model


data class FeedItem(var task: Task, var profile: Profile, var text: String, var createdAt: String, val event: EventType)

data class Feeds(val feedItems: List<FeedItem>, val tasks: Map<Task, List<Int>>, val profiles: Map<Profile, List<Int>>)
