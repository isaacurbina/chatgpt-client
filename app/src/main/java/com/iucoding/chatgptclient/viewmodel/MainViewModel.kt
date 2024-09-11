package com.iucoding.chatgptclient.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iucoding.chatgptclient.actions.UIEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _uiState = mutableStateOf(MainState())
    val uiState: State<MainState> = _uiState

    fun sendEvent(action: UIEvent) {
        when (action) {
            is UIEvent.Search -> handleSearch(action)
        }
    }

    private fun handleSearch(action: UIEvent.Search) {
        viewModelScope.launch(dispatcher) {
            _uiState.value = MainState(
                question = action.question,
                response = null
            )
            _uiState.value = _uiState.value.copy(
                response = askChatGpt(action.question)
            )
        }
    }

    private fun askChatGpt(prompt: String): String {
        TODO("Add chat gpt prompt here, continue https://www.geeksforgeeks.org/how-to-build-a-chatgpt-like-app-in-android-using-openai-api/#")
    }
}
