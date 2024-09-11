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
import androidx.compose.runtime.State
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iucoding.chatgptclient.R
import com.iucoding.chatgptclient.actions.UIEvent
import com.iucoding.chatgptclient.viewmodel.MainState
import com.iucoding.chatgptclient.viewmodel.MainViewModel
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    MainScreen(
        state = viewModel.uiState,
        onEvent = viewModel::sendEvent,
        toast = viewModel.toast,
        modifier = modifier
    )
}

@Composable
fun MainScreen(
    state: State<MainState>,
    onEvent: (event: UIEvent) -> Unit,
    toast: SharedFlow<UiText>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    ObserveAsEvents(flow = toast) {
        Toast.makeText(
            context,
            it.asString(context),
            Toast.LENGTH_LONG
        ).show()
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(5.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        // Text view to display question
        state.value.question?.let {
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
        state.value.response?.let {
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
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            var userInput by remember { mutableStateOf("Hello") }
            TextField(
                value = "",
                placeholder = {
                    Text(text = stringResource(id = R.string.question_placeholder))
                },
                onValueChange = {
                    userInput = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(size = 8.dp)
                    )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedIconButton(
                onClick = {
                    onEvent(UIEvent.Search(prompt = userInput))
                }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = android.R.drawable.ic_menu_send),
                    contentDescription = stringResource(id = R.string.send_content_description),
                    tint = Color.Green
                )
            }
        }
    }
}
