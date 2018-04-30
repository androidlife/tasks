package com.tasks

import com.tasks.model.FeedsTransformer
import com.tasks.network.TAG_PROFILE_NAME
import com.tasks.network.TAG_TASK_NAME
import com.tasks.network.URL_BASE

object Injection {
    fun getFeedsTransformer(): FeedsTransformer {
        return FeedsTransformer(URL_BASE, TAG_PROFILE_NAME, TAG_TASK_NAME)
    }
}
