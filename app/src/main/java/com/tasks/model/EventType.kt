package com.tasks.model

const val EVENT_ASSIGNED = "assigned"
const val EVENT_POST = "post"
const val EVENT_COMMENT = "comment"
const val EVENT_COMPLETED = "completed"

enum class EventType( type: String) {
    Assigned(EVENT_ASSIGNED),
    Post(EVENT_POST),
    Comment(EVENT_COMMENT),
    Completed(EVENT_COMPLETED);

    companion object {
        fun getInstance(type: String): EventType {
            return when (type) {
                EVENT_ASSIGNED -> Assigned
                EVENT_COMMENT -> Comment
                EVENT_COMPLETED -> Completed
                else -> Post
            }
        }
    }
}
