package com.iucoding.chatgptclient.repository

import com.iucoding.chatgptclient.composable.UiText
import com.iucoding.chatgptclient.networking.DataError
import com.iucoding.chatgptclient.networking.Result
import io.ktor.client.engine.cio.CIO

interface ChatGptFacade {
    suspend fun prompt(query: String): Result<UiText, DataError.Network>

    companion object {
        fun getInstance(engine: Engine): ChatGptFacade = when (engine) {
            Engine.GPT_4O_MINI -> OpenAiGpt4oMiniFacadeImpl(CIO.create())
            Engine.GENERATIVE_AI -> GoogleGenerativeAiFacadeImpl()
        }
    }

    enum class Engine {
        GPT_4O_MINI,
        GENERATIVE_AI
    }
}
