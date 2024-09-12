package com.iucoding.chatgptclient.repository

import com.iucoding.chatgptclient.composable.UiText
import com.iucoding.chatgptclient.model.ChatGPTResponse
import com.iucoding.chatgptclient.networking.DataError
import com.iucoding.chatgptclient.networking.Result
import io.ktor.client.engine.cio.CIO

interface ChatGptFacade {
    suspend fun prompt(query: String): Result<ChatGPTResponse, DataError.Network>

    companion object {
        fun getInstance(): ChatGptFacade = ChatGptFacadeImpl(CIO.create())
    }
}
