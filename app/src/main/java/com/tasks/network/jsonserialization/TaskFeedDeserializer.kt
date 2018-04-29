package com.tasks.network.jsonserialization

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.tasks.model.Feed
import com.tasks.model.TaskFeed
import java.lang.reflect.Type

class TaskFeedDeserializer() : JsonDeserializer<TaskFeed> {
    private val TASK_ID = "task_id"
    private val PROFILE_ID = "profile_id"
    private val TEXT = "text"
    private val CREATED_AT = "created_at"
    private val EVENT = "event"

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): TaskFeed {
        val feeds = ArrayList<Feed>()
        val profileIds = HashSet<Int>()
        val taskIds = HashSet<Int>()

        json?.asJsonArray?.onEach {
            val jsonObject = it.asJsonObject
            val taskId = jsonObject.get(TASK_ID).asInt
            val profileId = jsonObject.get(PROFILE_ID).asInt
            profileIds.add(profileId)
            taskIds.add(taskId)

            val text = jsonObject.get(TEXT).asString
            val createdAt = jsonObject.get(CREATED_AT).asString
            val event = jsonObject.get(EVENT).asString
            feeds.add(Feed(taskId, profileId, text, createdAt, event))
        }

        return TaskFeed(feeds, taskIds, profileIds)
    }

}
