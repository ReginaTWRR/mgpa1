package com.regina.mgpa1.ErrorUIs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class EDefaulUI: ErrorUIBase() {
    @Composable
    override fun ErrorResolution() {
        Text("Hello! Something went wrong. please try refreshing or restarting the app. Please Contact us! @CustomerSupport", textAlign = TextAlign.Center)
    }
}