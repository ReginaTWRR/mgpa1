package com.regina.mgpa1.games.higherLower

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.regina.mgpa1.sections.AppSection

//==CLASSES

// A class that implements the lost Higher Lower game
class LostHigherLowerGame : AppSection(header = "Lost Higher/Lower") {
    @Composable
    override fun SectionLayout() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Game Over",
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(modifier = Modifier.height(10.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(size = 10.dp)
                )
            ) {
                Text(
                    text = "FINAL STREAK",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(all = 10.dp)
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}
