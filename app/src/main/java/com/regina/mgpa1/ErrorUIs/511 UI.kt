package com.regina.mgpa1.ErrorUIs

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

class E511UI: ErrorUIBase() {
    @Composable
    override fun ErrorResolution() {
        Text("Please Sign into your Wifi", textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(10.dp))
        RefreshButton()
    }
}
