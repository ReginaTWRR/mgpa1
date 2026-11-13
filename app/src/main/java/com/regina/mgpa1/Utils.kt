package com.regina.mgpa1

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

//==FUNCTIONS

//A Composable function to render a level's name and position
@Composable
fun RowScope.RenderLevelName(
    level: Level,
    weight: Float = 0f
) {
    val text = "#${level.position} - ${level.name}"

    if (weight > 0f) {
        Text(
            text = text,
            modifier = Modifier.weight(weight)
        )
    }
    else {
        Text(text = text)
    }
}

//A Composable function to show the user a message
@Composable
fun RenderMessage(text: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

