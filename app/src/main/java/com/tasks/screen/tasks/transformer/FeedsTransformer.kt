package com.tasks.screen.tasks.transformer

import com.tasks.model.FeedItem
import com.tasks.model.Feeds
import com.tasks.model.Profile
import com.tasks.model.Task
import com.tasks.network.TaskService
import io.reactivex.Single
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers

class FeedsTransformer(private val urlBase: String, private val tagProfileName: String, private val tagTaskName: String) {
    fun combineFeedsWithRemoteCalls(feeds: Feeds, taskService: TaskService): Triple<Single<Feeds>, Single<List<Profile>>, Single<List<Task>>> {
        val profilesRequest = ArrayList<Single<Profile>>()
        feeds.profiles.keys.forEach { profile: Profile ->
            val profileFetchTask: Single<Profile> = taskService.getProfile(profile.id).onErrorReturn { t -> Profile.getEmpty(profile.id) }
            profilesRequest.add(profileFetchTask)
        }
        val tasksRequest = ArrayList<Single<Task>>()
        feeds.tasks.keys.forEach { task: Task ->
            val taskFetchTask: Single<Task> = taskService.getTask(task.id).onErrorReturn { t -> Task.getEmpty(task.id) }
            tasksRequest.add(taskFetchTask)
        }

        val profilesRequestCombined = Single.merge(profilesRequest).toList().subscribeOn(Schedulers.io())
        val tasksRequestCombined = Single.merge(tasksRequest).toList().subscribeOn(Schedulers.io())
        return Triple(Single.just(feeds), profilesRequestCombined, tasksRequestCombined)
    }

    fun zipProfilesAndTasksCall(triple: Triple<Single<Feeds>, Single<List<Profile>>, Single<List<Task>>>): Single<List<FeedItem>> {
        return Single.zip(
                triple.first,
                triple.second,
                triple.third,
                Function3<Feeds, List<Profile>, List<Task>, List<FeedItem>>
                { feeds, profiles, tasks ->
                    profiles.forEach { profile ->
                        profile.profileUrl = urlBase.plus(profile.profileUrl)
                        feeds.profiles[profile]?.forEach {
                            feeds.feedItems[it].profile = profile
                            feeds.feedItems[it].text = feeds.feedItems[it].text.replace(tagProfileName, profile.firstName)
                        }

                    }
                    tasks.forEach { task ->
                        feeds.tasks[task]?.forEach {
                            feeds.feedItems[it].task = task
                            feeds.feedItems[it].text = feeds.feedItems[it].text.replace(tagTaskName, "\"".plus(task.name).plus("\""))
                        }
                    }
                    feeds.feedItems
                })
    }
}
