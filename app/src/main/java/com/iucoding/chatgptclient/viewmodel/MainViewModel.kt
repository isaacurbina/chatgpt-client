package com.iucoding.chatgptclient.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iucoding.chatgptclient.R
import com.iucoding.chatgptclient.actions.UIEvent
import com.iucoding.chatgptclient.composable.UiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _toast = MutableSharedFlow<UiText>()
    val toast: SharedFlow<UiText> = _toast.asSharedFlow()

    private val _uiState = mutableStateOf(MainState())
    val uiState: State<MainState> = _uiState

    fun sendEvent(action: UIEvent) {
        when (action) {
            is UIEvent.Search -> handleSearch(action)
        }
    }

    private fun handleSearch(action: UIEvent.Search) {
        viewModelScope.launch(dispatcher) {
            if (action.prompt.isEmpty()) {
                _toast.emit(UiText.StringResource(id = R.string.prompt_is_empty))
            }
            _uiState.value = MainState(
                question = UiText.DynamicString(value = action.prompt),
                response = null
            )
            val response = askChatGpt(action.prompt)
            _uiState.value = _uiState.value.copy(
                response = response
            )
        }
    }

    private fun askChatGpt(prompt: String): UiText {
        TODO("Add chat gpt prompt here, continue https://www.geeksforgeeks.org/how-to-build-a-chatgpt-like-app-in-android-using-openai-api/#")
    }
}
