package com.iucoding.chatgptclient.composable

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.iucoding.chatgptclient.composable.UiText.DynamicString

sealed interface UiText {

    data class DynamicString(
        val value: String
    ) : UiText

    class StringResource(
        @StringRes val id: Int,
        val args: Array<Any> = arrayOf()
    ) : UiText

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(id = id, *args)
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(id, *args)
        }
    }
}

fun String.toUiText() = DynamicString(this)

fun Int.toUiText(args: Array<Any> = arrayOf()) = UiText.StringResource(this, args)
