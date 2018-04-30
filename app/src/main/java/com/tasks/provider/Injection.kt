package com.tasks.provider

import com.google.gson.GsonBuilder
import com.tasks.BuildConfig
import com.tasks.model.Feeds
import com.tasks.model.transformer.FeedsTransformer
import com.tasks.network.*
import com.tasks.network.jsonserialization.TaskFeedDeserializer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Injection {
    fun getFeedsTransformer(): FeedsTransformer {
        return FeedsTransformer(URL_BASE, TAG_PROFILE_NAME, TAG_TASK_NAME)
    }

    fun getTaskService(): TaskService {
        return ApiManager.taskService
    }

    fun getRetrofit(): Retrofit {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        builder.connectTimeout(1, TimeUnit.MINUTES)
        builder.readTimeout(1, TimeUnit.MINUTES)
        if (BuildConfig.DEBUG)
            builder.addInterceptor(getLoggingInterceptor(HttpLoggingInterceptor.Level.BODY))
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(Feeds::class.java, TaskFeedDeserializer())
        return Retrofit.Builder()
                .baseUrl(URL_BASE)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    private fun getLoggingInterceptor(level: HttpLoggingInterceptor.Level): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = level
        return loggingInterceptor
    }
}
