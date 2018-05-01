package com.tasks.model

/**
 * An error class for our app
 * Here we are using two types of errors only
 * Fetch and Network
 * If other error types are encountered, we keep here
 * This class can later be used to show relevant error message
 * as per the type
 */
class Error(val errorType: ErrorType) {
    enum class ErrorType {
        Fetch,
        Network
    }
}
