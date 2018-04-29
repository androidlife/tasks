package com.tasks.network

import com.tasks.model.Profile
import com.tasks.model.Task
import com.tasks.model.TaskFeed
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET(URL_PROFILE)
    fun getProfile(@Path(PATH_PROFILE) profileId: Int): Single<Profile>

    @GET(URL_TASK)
    fun getTask(@Path(PATH_TASK) taskId: Int): Single<Task>

    @GET(URL_TASKS_FEED)
    fun getTasksFeed(): Single<TaskFeed>
}
