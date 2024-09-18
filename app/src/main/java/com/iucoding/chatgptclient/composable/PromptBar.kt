package com.iucoding.chatgptclient.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.iucoding.chatgptclient.R
import com.iucoding.chatgptclient.actions.UIEvent

@Composable
fun PromptBar(
    onEvent: (event: UIEvent) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        var textFieldValue by remember {
            mutableStateOf(TextFieldValue(""))
        }
        TextField(
            value = textFieldValue,
            placeholder = {
                Text(text = stringResource(id = R.string.question_placeholder))
            },
            onValueChange = {
                textFieldValue = it
            },
            enabled = enabled,
            modifier = Modifier
                .weight(1f)
                .background(
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(size = 8.dp)
                )
        )
        Spacer(modifier = Modifier.width(16.dp))
        OutlinedIconButton(
            onClick = {
                onEvent(UIEvent.Search(prompt = textFieldValue.text))
                textFieldValue = TextFieldValue("")
            },
            enabled = enabled
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_send),
                contentDescription = stringResource(id = R.string.send_content_description),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@PreviewScreenSizes
@Composable
private fun PromptBarPreview() {
    PromptBar(
        enabled = true,
        onEvent = {},
        modifier = Modifier.fillMaxWidth()
    )
}
