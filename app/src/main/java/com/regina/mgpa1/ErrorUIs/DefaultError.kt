package com.regina.mgpa1.ErrorUIs

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign

class EDefaulUI: ErrorUIBase() {
    @Composable
    override fun ErrorResolution() {
        Text("Hello! Something went wrong. please try refreshing or restarting the app. $contactMessage", textAlign = TextAlign.Center)
    }
}
