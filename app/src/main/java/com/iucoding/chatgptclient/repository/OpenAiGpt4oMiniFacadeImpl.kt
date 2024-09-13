package com.iucoding.chatgptclient.repository

import com.iucoding.chatgptclient.BuildConfig
import com.iucoding.chatgptclient.composable.UiText
import com.iucoding.chatgptclient.composable.toUiText
import com.iucoding.chatgptclient.model.ChatGPTRequest
import com.iucoding.chatgptclient.model.ChatGPTResponse
import com.iucoding.chatgptclient.networking.DataError
import com.iucoding.chatgptclient.networking.Result
import com.iucoding.chatgptclient.networking.post
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.util.concurrent.TimeUnit

class OpenAiGpt4oMiniFacadeImpl(
    engine: HttpClientEngine,
) : ChatGptFacade {

    private val httpClient: HttpClient by lazy {
        HttpClient(engine) {
            install(ContentNegotiation) {
                json(json = Json {
                    ignoreUnknownKeys = true
                })
            }
            install(HttpTimeout) {
                requestTimeoutMillis = TimeUnit.SECONDS.toMillis(30)
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.d(message)
                    }
                }
                level = LogLevel.ALL
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        val token = BuildConfig.CHAT_GPT_API_KEY
                        BearerTokens(
                            accessToken = token,
                            refreshToken = token
                        )
                    }
                }
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
    }

    override suspend fun prompt(query: String): Result<UiText, DataError.Network> {
        val response = httpClient.post<ChatGPTRequest, ChatGPTResponse>(
            requestUrl = CHAT_GPT_REQUEST_URL,
            body = ChatGPTRequest(
                prompt = query,
                model = "gpt-4o-mini",
                temperature = 0
            )
        )
        return when (response) {
            is Result.Success -> Result.Success(response.data.toString().toUiText())
            is Result.Error -> response
        }
    }

    companion object {
        private const val CHAT_GPT_REQUEST_URL = "https://api.openai.com/v1/chat/completions"
    }
}
