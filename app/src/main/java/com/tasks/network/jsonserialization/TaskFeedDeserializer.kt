package com.tasks.network.jsonserialization

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.tasks.model.*
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class TaskFeedDeserializer : JsonDeserializer<Feeds> {
    private val TASK_ID = "task_id"
    private val PROFILE_ID = "profile_id"
    private val TEXT = "text"
    private val CREATED_AT = "created_at"
    private val EVENT = "event"

    private val convertDateFormat = SimpleDateFormat("EEE hh:mm aaa", Locale.ENGLISH)
    private val originalDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SSX", Locale.ENGLISH)

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
            val createdAt = getFormattedDate(jsonObject.get(CREATED_AT).asString)
            val event = jsonObject.get(EVENT).asString
            feeds.add(FeedItem(task, profile, text, createdAt, EventType.getInstance(event)))
            ++index
        }

        return Feeds(feeds, tasks, profiles)
    }

    private fun getFormattedDate(dateTime: String): String {
        return try {
            val date: Date = originalDateFormat.parse(dateTime)
            convertDateFormat.format(date).toString()
                    .replace("AM", "am")
                    .replace("PM", "pm")
        } catch (e: Exception) {
            dateTime
        }
    }

}
