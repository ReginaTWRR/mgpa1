package com.regina.mgpa1

import android.bluetooth.BluetoothHidDeviceAppQosSettings
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.regina.mgpa1.games.HigherLowerGame

//==CLASSES

//A class that implements the Games section of the App. Designed with the
//consideration of adding more games in the future, though there is currently only one.
class GamesAppSection : AppSection(header = "Games") {
    @Preview
    @Composable
    override fun SectionLayout() {
        // Need to declare currentPage before defining the overviewPage because of
        // onGameClick() dependency. Just initialise with null first.
        var currentPage by remember {
            mutableStateOf<GamesPage?>(value = null)
        }

        // Same, need to declare overviewPage first, dependency in higher lower game.
        var overviewPage by remember {
            mutableStateOf<GamesPage?>(value = null)
        }

        // Define all games
        val games by remember {
            mutableStateOf(value = listOf<Game>(
                // Higher or lower
                Game(
                    name = "HIGHER OR LOWER",
                    page = GamesPage(
                        code = PageCode.HIGHERLOWER,
                        content = {
                            GameHigherLower(
                                onGameOver = {
                                    currentPage = overviewPage
                                }
                            )
                        }
                    )
                )
            ))
        }

        // Define the overview page
        overviewPage = GamesPage(
            code = PageCode.OVERVIEW,
            content = {
                GameOverview(
                    header, games,
                    onGameClick = { game ->
                        currentPage = game.page
                    }
                )
            }
        )

        // If page is null, "initialise" it to overviewPage.
        if (currentPage == null) {
            currentPage = overviewPage
        }

        // Store local reference to currentPage (Else smart cast doesn't work)
        val page = currentPage

        page ?: return
        page.content()
    }
}

//A data class that defines a games page
data class GamesPage(
    val code: PageCode,
    val content: @Composable () -> Unit
) {

}

//A data class that defines a game
data class Game(
    val name: String,
    val page: GamesPage
) {

}

//An enum class that stores a code for each page, used to reliably identify a page
enum class PageCode {
    OVERVIEW, HIGHERLOWER
}

//==FUNCTIONS

//A Composable function that renders the game overview
@Composable
fun GameOverview(header: String, games: List<Game>, onGameClick: (Game) -> Unit) {
    // Biggest rendering body
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
    ) {
        // Render a header on the top of the section to state the title
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = header,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Render the game cards
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = games
            ) { game ->
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

//A Composable function that renders the higher lower game
@Composable
fun GameHigherLower(onGameOver: (hasWon: Boolean) -> Unit) {
    HigherLowerGame(onGameOver).SectionLayout()
}

//A Composable function that defines a game card's appearance and function
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
            .fillMaxHeight(fraction = 0.25f)
    ) {
        Text(
            text = gameName,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge
        )
    }
}
