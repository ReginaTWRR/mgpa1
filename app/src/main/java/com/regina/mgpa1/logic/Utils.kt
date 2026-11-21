package com.regina.mgpa1.logic

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

//==INTERFACES
interface Searchable {
    fun doesMatchSearchQuery(query: String) : Boolean
}

//==FUNCTIONS

//A Composable function to render a level's name and position
@Composable
fun LevelName(level: Level, textColor: Color, modifier: Modifier) {
    Text(
        text = "#${level.position} - ${level.name}",
        color = textColor,
        modifier = modifier
    )
}

//A Composable function to show the user a message
@Composable
fun RenderMessage(text: String, modifier: Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge
        )
    }
}
