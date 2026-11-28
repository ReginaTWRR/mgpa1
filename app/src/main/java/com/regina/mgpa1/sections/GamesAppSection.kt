package com.regina.mgpa1.sections

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.regina.mgpa1.ErrorUIs.preLoadedUI
import com.regina.mgpa1.games.higherLower.MainHigherLowerGame
import com.regina.mgpa1.viewModels.Game
import com.regina.mgpa1.viewModels.GamesSectionViewModel
import com.regina.mgpa1.viewModels.GamesPage
import com.regina.mgpa1.viewModels.HigherLowerViewModel
import com.regina.mgpa1.viewModels.PageCode

//==CLASSES

// A class that implements the Games section of the App. Designed with the
// consideration of adding more games in the future, though there is currently only one.
class GamesAppSection : AppSection(header = "Games") {
    @Composable
    override fun SectionLayout() {
        val gamesViewModel = viewModel<GamesSectionViewModel>()
        val currentPage = gamesViewModel.currentPage.collectAsState().value

        val hlViewModel = viewModel<HigherLowerViewModel>()
        val higherLowerGame = hlViewModel.game.collectAsState().value

        // Need to declare overviewPage first. Dependency in higher lower game
        var overviewPage by remember {
            mutableStateOf<GamesPage?>(value = null)
        }

        // Lambda function to return to the overview page
        val returnOverviewPage: () -> Unit = {
            val newPage = overviewPage

            if (newPage != null) {
                gamesViewModel.onPageChanged(newPage = newPage)
            }
        }

        // Define all games and their pages
        hlViewModel.initGame(returnOverviewPage)

        if (higherLowerGame == null) {
            preLoadedUI()
            return
        }

        val games by remember {
            mutableStateOf(value = listOf(higherLowerGame))
        }

        // Define the overview page
        overviewPage = GamesPage(
            code = PageCode.OVERVIEW,
            content = {
                GameOverview(
                    header, games,
                    onGameClick = { game ->
                        gamesViewModel.onPageChanged(newPage = game.page)
                        game.onEnter()
                    }
                )
            }
        )

        // If page is null, "initialise" it to overviewPage.
        if (currentPage == null) {
            overviewPage?.let{
                gamesViewModel.onPageChanged(newPage = it)
            }
        }

        Crossfade(
            targetState = currentPage
        ) { screen ->
            screen?.content()
        }
    }
}

//==FUNCTIONS

// A Composable function that renders the game overview
@Composable
fun GameOverview(header: String, games: List<Game>, onGameClick: (Game) -> Unit) {
    // Biggest rendering body
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        // Render a header on the top of the section to state the title
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = header,
                style = MaterialTheme.typography.titleLarge
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        // Render the game cards
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            items(items = games) { game ->
                GameCard(
                    gameName = game.name,
                    onClick = {
                        onGameClick(game)
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

// A Composable function that defines a game card's appearance and function
@Composable
fun GameCard(gameName: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onBackground
        ),
        modifier = Modifier
            .fillMaxWidth(fraction = 0.875f)
            .height(150.dp)
    ) {
        Text(
            text = gameName,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge
        )
    }
}
