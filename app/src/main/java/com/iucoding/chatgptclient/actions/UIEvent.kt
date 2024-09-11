package com.iucoding.chatgptclient.actions

sealed class UIEvent {
    data class Search(val question: String) : UIEvent()
}
