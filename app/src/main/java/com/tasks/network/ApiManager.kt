package com.tasks.network

object ApiManager {
    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}
