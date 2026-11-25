package com.regina.mgpa1.ErrorUIs

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign

class E404UI: ErrorUIBase() {
    @Composable
    override fun ErrorResolution() {
        Text("Hello! the AREDL might be down right now. It might also be an issue on our end. $contactMessage", textAlign = TextAlign.Center)
    }
}
