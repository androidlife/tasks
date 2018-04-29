package com.tasks.model

import com.tasks.network.TaskService
import io.reactivex.Single

class FeedsTransformer {
    fun transformToObservable(taskService: TaskService, feeds: Feeds): FeedsObservable {
        val tasksObservable = ArrayList<Single<Task>>()
        feeds.taskIds.forEach {
            tasksObservable.add(
                    taskService.getTask(it)
                            .onErrorReturn { t -> Task.getEmpty(it) })
        }
        val profilesObservable = ArrayList<Single<Profile>>()
        feeds.profileIds.forEach {
            profilesObservable.add(
                    taskService.getProfile(it)
                            .onErrorReturn { t -> Profile.getEmpty(it) })
        }
        return FeedsObservable(feeds.feedItems, tasksObservable, profilesObservable)
    }
}
