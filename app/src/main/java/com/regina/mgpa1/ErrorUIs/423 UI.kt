package com.regina.mgpa1.ErrorUIs

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign

class E423UI : ErrorUIBase() {
    @Composable
    override fun ErrorResolution() {
        Text("Hello! This is on us. $contactMessage", textAlign = TextAlign.Center);
    }
}
