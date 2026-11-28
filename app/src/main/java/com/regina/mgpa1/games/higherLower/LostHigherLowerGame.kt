package com.regina.mgpa1.games.higherLower

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.regina.mgpa1.ErrorUIs.preLoadedUI
import com.regina.mgpa1.sections.AppSection
import com.regina.mgpa1.viewModels.HigherLowerViewModel

//==CLASSES

// A class that implements the lost Higher Lower game
class LostHigherLowerGame(
    val returnOverviewPage: () -> Unit
) : AppSection(header = "Lost Higher/Lower") {
    @Composable
    override fun SectionLayout() {
        val viewModel = viewModel<HigherLowerViewModel>()
        val mainPage = viewModel.mainPage
        val streak = viewModel.streak.collectAsState().value

        if (mainPage == null)
        {
            preLoadedUI()
            return
        }

        BackHandler {
            // Go back to the Games section
            returnOverviewPage()
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.onBackground,
                        shape = RoundedCornerShape(size = 10.dp)
                    )
            ) {
                Text(
                    text = "Game Over",
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(horizontal = 100.dp)
                        .border(
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

                    Text(
                        text = "$streak",
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.padding(all = 10.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            viewModel.onPageChanged(newPage = mainPage)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onSecondary,
                            contentColor = MaterialTheme.colorScheme.inverseSurface
                        ),
                        modifier = Modifier.width(200.dp)
                    ) {
                        Text(
                            text = "Play Again",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))

                    Button(
                        onClick = {
                            returnOverviewPage()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.inverseSurface,
                            contentColor = MaterialTheme.colorScheme.background
                        ),
                        modifier = Modifier.width(200.dp)
                    ) {
                        Text(
                            text = "Return to Menu",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
