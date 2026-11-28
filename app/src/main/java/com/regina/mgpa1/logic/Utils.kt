package com.regina.mgpa1.logic

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

//==INTERFACES
interface Searchable {
    fun doesMatchSearchQuery(query: String) : Boolean
}

interface Playable {
    fun enterGame()
}

//==FUNCTIONS

// A Composable function to render a level's name and position
@Composable
fun LevelName(level: Level, textColor: Color, modifier: Modifier) {
    Text(
        text = "#${level.position} - ${level.name}",
        color = textColor,
        modifier = modifier
    )
}

// A Composable function to show the user a message
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

// A Composable function to render a standard title text
@Composable
fun StandardTitle(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.inverseSurface,   // Off-white
        style = MaterialTheme.typography.titleLarge
    )
}

// A Composable function to render a title with a brush (gradient)
@Composable
fun BrushTitle(text: String, colors: List<Color>) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge.copy(
            brush = Brush.linearGradient(colors = colors)
        )
    )
}

// A Composable function to render an outlined headline text
@Composable
fun OutlinedHeadline(
    text: String, outlinedColor: Color, fillColor: Color, outlineWidth: Dp
) {
    // Convert dp to px
    val width = with(receiver = LocalDensity.current) {
        outlineWidth.toPx()
    }

    Box {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineLarge.copy(
                color = outlinedColor,
                drawStyle = Stroke(
                    width = width
                )
            )
        )
        Text(
            text = text,
            style = MaterialTheme.typography.headlineLarge.copy(
                color = fillColor
            )
        )
    }
}

// A Composable function to render an animated number, does a vertical rolling animation
// depending on whether the number is increased or decreased.
@Composable
fun AnimatedNumber(number: Int, style: TextStyle) {
    AnimatedContent(
        targetState = number,
        transitionSpec = {
           if (targetState > initialState) {
               slideInVertically { height -> height } + fadeIn() togetherWith
                       slideOutVertically { height -> -height } + fadeOut()
           } else {
               slideInVertically { height -> -height } + fadeIn() togetherWith
                       slideOutVertically { height -> height } + fadeOut()
           }.using(
               SizeTransform(clip = false)
           )
        }
    ) { targetNumber ->
        Text(
            text = "$targetNumber",
            style = style
        )
    }
}
