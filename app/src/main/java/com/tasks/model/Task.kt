package com.tasks.model

import com.google.gson.annotations.SerializedName

data class Task(@SerializedName("id") val id: Int,
                @SerializedName("name") val name: String,
                @SerializedName("description") val description: String,
                @SerializedName("state") val state: String,
                @SerializedName("poster_id") val posterId: Int,
                @SerializedName("worker_id") val workedId: Int) {
    companion object {
        fun getEmpty(id: Int): Task {
            return Task(id, "", "", "", -1, -1)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is Task)
            return other.id == id
        return false
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}