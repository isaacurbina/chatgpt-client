package com.iucoding.chatgptclient.viewmodel

import com.iucoding.chatgptclient.composable.UiText

data class MainState(
    val question: UiText? = null,
    val response: UiText? = null,
    val isLoading: Boolean = false
)
