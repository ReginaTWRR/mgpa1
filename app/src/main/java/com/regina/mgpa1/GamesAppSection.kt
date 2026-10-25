package com.regina.mgpa1

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

//==CLASSES

//A class that implements the Games section of the App
class GamesAppSection : AppSection("Games") {
    @Composable
    override fun SectionLayout() {
        Column() {
            Text(text = header)
        }
    }
}
