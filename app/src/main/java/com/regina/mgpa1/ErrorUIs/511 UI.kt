package com.regina.mgpa1.ErrorUIs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.regina.mgpa1.viewModels.DemonListViewModel
import com.regina.mgpa1.viewModels.HomePageViewModel
import okhttp3.internal.wait

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