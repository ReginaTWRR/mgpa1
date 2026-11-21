package com.regina.mgpa1.ErrorUIs

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign

class E429UI : ErrorUIBase() {
    @Composable
    override fun ErrorResolution() {
        Text("Hello! You're doing that too often, please try again later.", textAlign = TextAlign.Center)
    }
}
