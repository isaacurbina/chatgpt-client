package com.iucoding.chatgptclient.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iucoding.chatgptclient.R
import com.iucoding.chatgptclient.actions.UIEvent
import com.iucoding.chatgptclient.ui.theme.ChatGPTClientTheme
import com.iucoding.chatgptclient.viewmodel.MainState
import com.iucoding.chatgptclient.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    ObserveAsEvents(flow = viewModel.toast) {
        scope.launch {
            snackBarHostState.showSnackbar(
                message = it.asString(context),
                actionLabel = context.getString(R.string.snack_ok),
                duration = SnackbarDuration.Short
            )
        }
    }
    MainScreen(
        state = viewModel.uiState.value,
        onEvent = viewModel::sendEvent,
        modifier = modifier
    )
}

@Composable
fun MainScreen(
    state: MainState,
    onEvent: (event: UIEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        // Text view to display question
        state.question?.let {
            Text(
                text = it.asString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        if (state.isLoading) {
            // Display progress indicator
            Box(
                modifier = Modifier
                    .weight(1.0f)
                    .fillMaxWidth()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(64.dp)
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        } else {
            // Text view to display response
            state.response?.let {
                // clearing prompt text field once an answer is received
                TypewriteText(
                    text = it.asString(),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .weight(1.0f)
                        .verticalScroll(rememberScrollState())
                )
            } ?: Spacer(modifier = Modifier.weight(1f))
        }
        PromptBar(
            enabled = !state.isLoading,
            onEvent = onEvent,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@PreviewScreenSizes
@Composable
private fun MainScreenPreview() {
    ChatGPTClientTheme {
        MainScreen(
            state = MainState(
                question = UiText.DynamicString("The prompt for ChatGPT goes here"),
                response = UiText.DynamicString("The answer should go here"),
                isLoading = false,
            ),
            onEvent = {}
        )
    }
}
