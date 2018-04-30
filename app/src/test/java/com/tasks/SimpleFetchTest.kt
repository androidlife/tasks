package com.tasks

import com.tasks.model.Profile
import com.tasks.model.Task
import com.tasks.network.TaskService
import com.tasks.provider.Injection
import io.reactivex.Single
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class SimpleFetchTest {


    lateinit var taskService: TaskService
    @Before
    fun initializeTaskService() {
        taskService = Injection.getTaskService()
    }

    @Test
    fun fetchFeeds() {
        val feedsTransformer = Injection.getFeedsTransformer()
        val feedItems = taskService.getTasksFeed().map { feeds ->
            feedsTransformer.combineFeedsWithRemoteCalls(feeds, taskService)
        }.flatMap { triple ->
            feedsTransformer.zipProfilesAndTasksCall(triple)
        }.blockingGet()
        assertTrue(feedItems != null && !feedItems.isEmpty())

    }


    //@Test
    fun fetchTaskFeed() {
        val taskFeed = taskService.getTasksFeed().blockingGet()
        assertTrue(taskFeed != null && taskFeed.feedItems.isNotEmpty() && taskFeed.profiles.isNotEmpty()
                && taskFeed.tasks.isNotEmpty())
    }

    //@Test
    fun fetchTasks() {
        val tasksList = ArrayList<Single<Task>>()
        for (i in 1..5)
            tasksList.add(taskService.getTask(i))
        val tasks = Single.merge(tasksList).toList().blockingGet()
        assertTrue(tasks != null && tasks.isNotEmpty())
    }

    //@Test
    fun testErrorCaseForTasks() {
        val tasksList = ArrayList<Single<Task>>()
        for (i in -1..2)
            tasksList.add(taskService.getTask(i).onErrorReturn { t -> Task.getEmpty(i) })
        val tasks = Single.merge(tasksList).toList().blockingGet()
        assertTrue(tasks != null && tasks.isNotEmpty())
    }

    //@Test
    fun fetchProfile() {
        val profileList = ArrayList<Single<Profile>>()
        for (i in 1..5)
            profileList.add(taskService.getProfile(i))
        val profiles = Single.merge(profileList).toList().blockingGet()
        assertTrue(profiles != null && profiles.isNotEmpty())
    }

    //@Test
    fun testErrorCaseForProfile() {
        val profileList = ArrayList<Single<Profile>>()
        for (i in -1..2)
            profileList.add(taskService.getProfile(i).onErrorReturn { t -> Profile.getEmpty(i) })
        val profiles = Single.merge(profileList).toList().blockingGet()
        assertTrue(profiles != null && profiles.isNotEmpty())
    }

}

