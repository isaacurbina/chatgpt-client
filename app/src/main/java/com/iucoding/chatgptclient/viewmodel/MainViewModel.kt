package com.iucoding.chatgptclient.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iucoding.chatgptclient.R
import com.iucoding.chatgptclient.actions.UIEvent
import com.iucoding.chatgptclient.composable.UiText
import com.iucoding.chatgptclient.networking.Result
import com.iucoding.chatgptclient.networking.asUiText
import com.iucoding.chatgptclient.repository.ChatGptFacade
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val facade: ChatGptFacade,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _toast = MutableSharedFlow<UiText>()
    val toast: SharedFlow<UiText> = _toast.asSharedFlow()

    private val _uiState = mutableStateOf(
        MainState()
    )
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
                return@launch
            }
            _uiState.value = MainState(
                question = UiText.DynamicString(value = action.prompt),
                isLoading = true
            )
            val response = askChatGpt(action.prompt)
            _uiState.value = _uiState.value.copy(
                response = response,
                isLoading = false
            )
        }
    }

    private suspend fun askChatGpt(query: String): UiText {
        return when (val result = facade.prompt(query)) {
            is Result.Error -> result.error.asUiText()
            is Result.Success -> result.data
        }
    }
}
