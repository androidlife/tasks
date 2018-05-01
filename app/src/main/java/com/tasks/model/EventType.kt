package com.tasks.model

const val EVENT_ASSIGNED = "assigned"
const val EVENT_POST = "post"
const val EVENT_COMMENT = "comment"
const val EVENT_COMPLETED = "completed"

/**
 * This class is direct mapping of event tag of feed json
 * So whatever value comes in event tag of feed json, we keep
 * it here and as per the value we add new enum type
 * The main reason for doing this is that if needed later on
 * we can show different row items on recycler view as per the
 * event type and it will be easier for view to show relevant
 * view type as per the event
 */
enum class EventType(type: String) {
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
