package com.iucoding.chatgptclient.networking

sealed interface DataError : Error {

    enum class Network : DataError {
        REQUEST_TIMEOUT,
        BAD_REQUEST,
        UNAUTHORIZED,
        NOT_FOUND,
        CONFLICT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERIALIZATION,
        UNKNOWN
    }

    enum class Local : DataError {
        DISK_FULL
    }
}
