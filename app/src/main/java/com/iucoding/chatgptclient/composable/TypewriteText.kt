package com.iucoding.chatgptclient.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import timber.log.Timber

@Composable
fun TypewriteText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    spec: AnimationSpec<Int> = tween(durationMillis = text.length * 100, easing = LinearEasing),
    style: TextStyle = LocalTextStyle.current,
    preoccupySpace: Boolean = true
) {
    // state that keeps the text that is currently animated
    var textToAnimate by remember { mutableStateOf("") }
    // index to control progress of the animation
    val index = remember {
        Animatable(initialValue = 0, typeConverter = Int.VectorConverter)
    }
    // effect to handle animation when text content changes
    LaunchedEffect(key1 = text) {
        Timber.tag("TypewriteText").i(message = "text changed to $text")
        try {
            if (text.isNotEmpty()) {
                index.snapTo(0)
                textToAnimate = text
                index.animateTo(text.length, spec)
            }
        } catch (e: Exception) {
            Timber.tag("TypewriteText").e(e)
        }
    }
    // container for animated and static text
    Box(modifier = modifier) {
        if (preoccupySpace && index.isRunning) {
            Timber.tag("TypewriteText").i(message = "preoccupy is true and index is running")
            // display invisible text when preoccupy is true and animation is in progress
            // placeholder for the space the text will use up
            Text(
                text = text,
                style = style,
                color = color,
                fontSize = fontSize,
                modifier = Modifier.alpha(0f)
            )
        }

        Timber.tag("TypewriteText").i(message = "text rendering $textToAnimate")
        // display animated text based on current index value
        Text(
            text = textToAnimate.substring(0, index.value),
            style = style,
            color = color,
            fontSize = fontSize,
        )
    }
}
