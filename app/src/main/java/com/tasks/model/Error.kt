package com.tasks.model

class Error(val errorType: ErrorType) {
    enum class ErrorType {
        Fetch,
        Network
    }
}
