package com.iucoding.chatgptclient.composable

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iucoding.chatgptclient.R
import com.iucoding.chatgptclient.actions.UIEvent
import com.iucoding.chatgptclient.ui.theme.ChatGPTClientTheme
import com.iucoding.chatgptclient.viewmodel.MainState
import com.iucoding.chatgptclient.viewmodel.MainViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    ObserveAsEvents(flow = viewModel.toast) {
        Toast.makeText(
            context,
            it.asString(context),
            Toast.LENGTH_LONG
        ).show()
    }
    MainScreen(
        state = viewModel.uiState.value,
        onEvent = viewModel::sendEvent,
        onError = viewModel::onError,
        modifier = modifier
    )
}

@Composable
fun MainScreen(
    state: MainState,
    onEvent: (event: UIEvent) -> Unit,
    onError: (error: UiText) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(5.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        // Text view to display question
        state.question?.let {
            Text(
                text = it.asString(),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
        }
        // Text view to display response
        state.response?.let {
            Text(
                text = it.asString(),
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .weight(1.0f)
                    .verticalScroll(rememberScrollState())
            )
        } ?: Spacer(modifier = Modifier.weight(1f))
        // TODO("Add spinner while waiting for response")
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            var textFieldValue by remember {
                mutableStateOf(TextFieldValue(text = "What is ChatGPT"))
            }
            TextField(
                value = textFieldValue,
                placeholder = {
                    Text(text = stringResource(id = R.string.question_placeholder))
                },
                onValueChange = {
                    textFieldValue = it
                },
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(size = 8.dp)
                    )
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedIconButton(
                modifier = modifier,
                onClick = {
                    onEvent(UIEvent.Search(prompt = textFieldValue.text))
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_send),
                    contentDescription = stringResource(id = R.string.send_content_description),
                    tint = Color.Green
                )
            }
        }
    }
}

@PreviewScreenSizes
@Composable
private fun MainScreenPreview() {
    ChatGPTClientTheme {
        MainScreen(
            state = MainState(
                question = UiText.DynamicString("The prompt for ChatGPT goes here"),
                response = UiText.DynamicString("The answer should go here")
            ),
            onEvent = {},
            onError = {}
        )
    }
}
