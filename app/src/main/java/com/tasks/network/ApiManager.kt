package com.tasks.network

object ApiManager {
    val taskService: TaskService = getRetrofit().create(TaskService::class.java)
}
