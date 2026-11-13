package com.regina.mgpa1

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

//==CLASSES

//A class that implements the Home section of the App
class HomeAppSection : AppSection(header = "Home") {
    @Composable
    override fun SectionLayout() {
        val scope = rememberCoroutineScope()

        // Biggest rendering body
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.9f)
                    .fillMaxHeight()
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
                SearchBar()
                Spacer(modifier = Modifier.height(10.dp))
                DemonOfTheDay()
                Spacer(modifier = Modifier.height(10.dp))
                DailyChallenge()
            }
        }
    }
}

//==FUNCTIONS

//A Composable function that renders the search bar
@Composable
fun SearchBar() {
    var text by rememberSaveable {
        mutableStateOf(value = "")
    }

    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxHeight(fraction = 0.075f)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(size = 10.dp)
            )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.search),
            contentDescription = "Search Icon",
            modifier = Modifier
                .height(50.dp)
                .width(50.dp)
        )

        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            placeholder = {
              Text(text = "Search for a demon...")
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                cursorColor = MaterialTheme.colorScheme.onBackground,
                focusedIndicatorColor = MaterialTheme.colorScheme.background,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onSecondary,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSecondary
            ),
            modifier = Modifier.weight(1f)
        )
    }
}

//A Composable function that renders the demon of the day
@Composable
fun DemonOfTheDay() {
    val demon = produceState<Level?>(
        initialValue = null
    ) {
        value = getRandomLevel()
    }.value

    if (demon == null) {
        RenderMessage(text = "Loading demon...")
        return
    }

    Column (
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Demon of the Day",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        AsyncImage(
            model = GetLevelThumbnail(LevelID = demon.level_id),
            contentDescription = "Demon"
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            RenderLevelName(
                level = demon,
                weight = 0.4f
            )

            Button(
                onClick = {

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.background
                ),
                modifier = Modifier.weight(0.4f)
            ) {
                Text(text = "WATCH GAMEPLAY")

                Icon(
                    painter = painterResource(id = R.drawable.play_circle),
                    contentDescription = "Play Button"
                )
            }
        }
    }
}

//A Composable function that renders the daily challenge
@Composable
fun DailyChallenge() {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "DAILY CHALLENGE",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.stadia_controller),
                contentDescription = "Game Icon"
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(text = "Higher or Lower")

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = {

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Text(text = "PLAY")
            }
        }

        Text(
            text = "Can you guess which demon is harder?",
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}
