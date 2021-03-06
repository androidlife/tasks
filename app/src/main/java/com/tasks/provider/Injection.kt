package com.tasks.provider

import android.content.Context
import com.google.gson.GsonBuilder
import com.tasks.BuildConfig
import com.tasks.model.Feeds
import com.tasks.network.TAG_PROFILE_NAME
import com.tasks.network.TAG_TASK_NAME
import com.tasks.network.TaskService
import com.tasks.network.URL_BASE
import com.tasks.network.jsonserialization.TaskFeedDeserializer
import com.tasks.screen.tasks.ListContract
import com.tasks.screen.tasks.ListModel
import com.tasks.screen.tasks.transformer.FeedsTransformer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 *  Just a simple Injection class to provide instances to needed classes
 */
object Injection {
    //singleton taskService
    val taskService: TaskService = getRetrofit().create(TaskService::class.java)
    private var fontProvider: FontProvider? = null

    fun getFeedsTransformer(): FeedsTransformer {
        return FeedsTransformer(URL_BASE, TAG_PROFILE_NAME, TAG_TASK_NAME)
    }

    //trying to create a singleton instance of FontProvider
    fun getFontProvider(context: Context): FontProvider? {
        if (fontProvider == null)
            fontProvider = FontProvider(context.assets)
        return fontProvider
    }

    fun getListModel(): ListContract.Model {
        return ListModel(taskService, getFeedsTransformer())
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
