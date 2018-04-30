package com.tasks.network

import com.tasks.provider.Injection

object ApiManager {
    val taskService: TaskService = Injection.getRetrofit().create(TaskService::class.java)
}
