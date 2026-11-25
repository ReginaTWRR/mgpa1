package com.regina.mgpa1.ErrorUIs

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign

class E410UI: ErrorUIBase() {
    @Composable
    override fun ErrorResolution() {
        Text("Hello! The link to Aredl might be deleted or moved. $contactMessage", textAlign = TextAlign.Center)
    }
}
