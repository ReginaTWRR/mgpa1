package com.regina.mgpa1.ErrorUIs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

class E408UI: ErrorUIBase() {
    @Composable
    override fun ErrorResolution() {
//        Text("Hello! the AREDL might be down right now. It might also be an issue on our end. Please Contact us! @CustomerSupport", textAlign = TextAlign.Center)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Hello! We failed to process your request in time. Please try again.",
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            RefreshButton()
        }
    }
}
