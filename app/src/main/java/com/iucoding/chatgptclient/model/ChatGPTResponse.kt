package com.iucoding.chatgptclient.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatGPTResponse(
    val choices: List<String?>?
)

