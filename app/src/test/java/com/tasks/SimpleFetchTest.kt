package com.tasks

import com.tasks.model.Profile
import com.tasks.model.Task
import com.tasks.network.ApiManager
import io.reactivex.Single
import org.junit.Assert.assertTrue
import org.junit.Test


class SimpleFetchTest {
    //@Test
    fun fetchTaskFeed() {
        val taskFeed = ApiManager.taskService.getTasksFeed().blockingGet()
        assertTrue(taskFeed != null && taskFeed.feeds.isNotEmpty() && taskFeed.profileIds.isNotEmpty()
                && taskFeed.taskIds.isNotEmpty())
    }

    //@Test
    fun fetchTasks() {
        val tasksList = ArrayList<Single<Task>>()
        for (i in 1..5)
            tasksList.add(ApiManager.taskService.getTask(i))
        val tasks = Single.merge(tasksList).toList().blockingGet()
        assertTrue(tasks != null && tasks.isNotEmpty())
    }

    @Test
    fun testErrorCaseForTasks() {
        val tasksList = ArrayList<Single<Task>>()
        for (i in -1..2)
            tasksList.add(ApiManager.taskService.getTask(i).onErrorReturn { t -> Task.getEmpty(i) })
        val tasks = Single.merge(tasksList).toList().blockingGet()
        assertTrue(tasks != null && tasks.isNotEmpty())
    }

    //@Test
    fun fetchProfile() {
        val profileList = ArrayList<Single<Profile>>()
        for (i in 1..5)
            profileList.add(ApiManager.taskService.getProfile(i))
        val profiles = Single.merge(profileList).toList().blockingGet()
        assertTrue(profiles != null && profiles.isNotEmpty())
    }

    //@Test
    fun testErrorCaseForProfile() {
        val profileList = ArrayList<Single<Profile>>()
        for (i in -1..2)
            profileList.add(ApiManager.taskService.getProfile(i).onErrorReturn { t -> Profile.getEmpty(i) })
        val profiles = Single.merge(profileList).toList().blockingGet()
        assertTrue(profiles != null && profiles.isNotEmpty())
    }

}
