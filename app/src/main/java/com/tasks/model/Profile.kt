package com.tasks.model

import com.google.gson.annotations.SerializedName

data class Profile(@SerializedName("id") val id: Int,
                   @SerializedName("first_name") val firstName: String,
                   @SerializedName("rating") val rating: Float,
                   @SerializedName("avatar_mini_url") var profileUrl: String) {
    companion object {
        fun getEmpty(id: Int): Profile {
            return Profile(id, "", 0f, "")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is Profile)
            return other.id == id
        return false
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}