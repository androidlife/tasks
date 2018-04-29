package com.tasks.model

import com.google.gson.annotations.SerializedName

data class Profile(@SerializedName("id") val id: Int,
                   @SerializedName("first_name") val firstName: String,
                   @SerializedName("rating") val rating: Float,
                   @SerializedName("avatar_mini_url") var profileUrl: String)

data class Task(@SerializedName("id") val id: Int,
                @SerializedName("name") val name: String,
                @SerializedName("description") val description: String,
                @SerializedName("state") val state: String,
                @SerializedName("poster_id") val posterId: Int,
                @SerializedName("worker_id") val workedId: Int)

data class Feed(val taskId: Int, val profileId: Int, var text: String, var createdAt: String, var event: String) {
    lateinit var profile: Profile
    lateinit var task: Task
}

data class TaskFeed(val feeds: List<Feed>, val taskIds: Set<Int>, val profileIds: Set<Int>)