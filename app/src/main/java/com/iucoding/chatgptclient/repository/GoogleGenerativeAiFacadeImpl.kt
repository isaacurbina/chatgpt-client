package com.iucoding.chatgptclient.repository

import com.google.ai.client.generativeai.GenerativeModel
import com.iucoding.chatgptclient.BuildConfig
import com.iucoding.chatgptclient.composable.UiText
import com.iucoding.chatgptclient.composable.toUiText
import com.iucoding.chatgptclient.networking.DataError
import com.iucoding.chatgptclient.networking.Result
import timber.log.Timber

class GoogleGenerativeAiFacadeImpl : ChatGptFacade {

    private val generativeModel: GenerativeModel by lazy {
        GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.GENERATIVE_AI_API_KEY
        )
    }

    override suspend fun prompt(query: String): Result<UiText, DataError.Network> {
        val result = try {
            val content = generativeModel.generateContent(query)
            content.text?.let {
                Result.Success(it.toUiText())
            } ?: Result.Error(DataError.Network.UNKNOWN)
        } catch (e: Exception) {
            Timber.tag("GoogleGenerativeAiFacadeImpl").e(e)
            Result.Error(DataError.Network.UNKNOWN)
        }
        return result
    }
}
