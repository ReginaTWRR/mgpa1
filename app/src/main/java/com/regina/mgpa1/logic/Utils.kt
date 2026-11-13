package com.regina.mgpa1.logic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

//==FUNCTIONS

//A Composable function to render a level's name and position
@Composable
fun RenderLevelName(
    level: Level,
    modifier: Modifier
) {
    Text(
        text = "#${level.position} - ${level.name}",
        modifier = modifier
    )
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
