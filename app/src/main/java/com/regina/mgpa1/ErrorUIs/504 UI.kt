package com.regina.mgpa1.ErrorUIs

import androidx.compose.runtime.Composable

class E504UI : ErrorUIBase() {
    @Composable
    override fun ErrorResolution() {
        ServerErrorUI(contactMessage)
    }
}
