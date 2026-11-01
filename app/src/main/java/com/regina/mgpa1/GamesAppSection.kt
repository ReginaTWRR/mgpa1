package com.regina.mgpa1

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
import com.regina.mgpa1.games.Higher_LowerGame

//==CLASSES

//A class that implements the Games section of the App
class GamesAppSection : AppSection(header = "Games") {
    @Preview
    @Composable
    override fun SectionLayout() {
        val games by remember {
            mutableStateOf(value = listOf<Game>(
                Game(name = "HIGHER OR LOWER")
            ))
        }

        var gameView by remember {
            mutableStateOf(value = GameView.NONE)
        }

        when (gameView) {
            GameView.NONE -> {
                GameOverview(
                    header, games,
                    onGameClick = { game ->
                        when (game.name) {
                            "HIGHER OR LOWER" -> gameView = GameView.HIGHERLOWER
                            else -> gameView = GameView.NONE
                        }
                    }
                )
            }
            GameView.HIGHERLOWER -> {
                GameHigherLower()
            }
        }
    }
}

//A data class that defines a game
data class Game(
    val name: String
) {

}

//An enum class to define the different games that the user can view
enum class GameView() {
    NONE, HIGHERLOWER
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
fun GameHigherLower() {
    Higher_LowerGame().SectionLayout()
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
