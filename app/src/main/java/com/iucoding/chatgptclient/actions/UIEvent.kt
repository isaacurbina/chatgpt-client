package com.iucoding.chatgptclient.actions

sealed class UIEvent {
    data class Search(val prompt: String) : UIEvent()
}
