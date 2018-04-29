package com.tasks.network.jsonserialization

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.tasks.model.FeedItem
import com.tasks.model.Feeds
import com.tasks.model.Profile
import com.tasks.model.Task
import java.lang.reflect.Type

class TaskFeedDeserializer() : JsonDeserializer<Feeds> {
    private val TASK_ID = "task_id"
    private val PROFILE_ID = "profile_id"
    private val TEXT = "text"
    private val CREATED_AT = "created_at"
    private val EVENT = "event"

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Feeds {
        val feeds = ArrayList<FeedItem>()
        val profiles = HashMap<Profile, ArrayList<Int>>()
        val tasks = HashMap<Task, ArrayList<Int>>()

        var index = 0

        json?.asJsonArray?.onEach {
            val jsonObject = it.asJsonObject
            val taskId = jsonObject.get(TASK_ID).asInt
            val profileId = jsonObject.get(PROFILE_ID).asInt

            val profile = Profile.getEmpty(profileId)
            if (profiles.contains(profile))
                profiles[profile]?.add(index)
            else
                profiles.put(profile, arrayListOf(index))


            val task = Task.getEmpty(taskId)
            if (tasks.contains(task))
                tasks[task]?.add(index)
            else
                tasks.put(task, arrayListOf(index))


            val text = jsonObject.get(TEXT).asString
            val createdAt = jsonObject.get(CREATED_AT).asString
            val event = jsonObject.get(EVENT).asString
            feeds.add(FeedItem(taskId, profileId, text, createdAt, event))
            ++index
        }

        return Feeds(feeds, tasks, profiles)
    }

}
