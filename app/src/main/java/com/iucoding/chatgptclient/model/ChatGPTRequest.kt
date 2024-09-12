package com.iucoding.chatgptclient.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatGPTRequest(
    val prompt: String,
    val model: String,
    val temperature: Int
)

