package com.regina.mgpa1.ErrorUIs

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.regina.mgpa1.viewModels.DemonListViewModel

class E511UI: ErrorUIBase() {
    @Composable
    override fun ErrorResolution() {
        Text("Please Sign into your Wifi", textAlign = TextAlign.Center)
        val DLviewModel = viewModel<DemonListViewModel>()
        Button(onClick = {
            DLviewModel.refreshLevelList()
        }) { Text("Refresh") }
    }
}
