package com.regina.mgpa1

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

//==CLASSES

//A class that implements the Demon section of the App
class DemonAppSection : AppSection("Demon List") {
    @Preview
    @Composable
    override fun SectionLayout() {
        // Get the list of all levels using the API layer's GetLevelList() function
        val levelsList = produceState(
            initialValue = emptyList<Level>()
        ) {
            value = GetLevelList()
        }

        // Biggest rendering body
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Render a header on the top of the section to state the header
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = header,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                )
            }

            // Render the list
            if (levelsList.value.isNotEmpty())
            {
                LevelList(levelsList.value)
            }
            else
            {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Loading levels...",
                        color = Color.White
                    )
                }
            }
        }
    }
}

//==FUNCTIONS

//A Composable function that renders all levels in the passed list
@Composable
fun LevelList(levels: List<Level>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .border(
                width = 1.dp,
                color = Color.White
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Hi!!! So, for now i made this into a lazy column and it just loads in a text. I
        //tried my best to optimise loading the list. Just saying, theres about 1k+ levels.
        //There are 2 options. A: Limit the list. B: Optimise the loading (somehow). C:Load once,
        //Store the info in memory(possible sideffects). You can try to explore your own solutions so yea
        //Good luck
        items(
            items = levels,
            key = { level -> level.id }
        ) { level ->
            LevelCard(level)
        }
    }
}

//A Composable function that defines what a level card should look like
@Composable
fun LevelCard(level: Level) {
    val levelDetails = produceState<LevelDetails?>(
        initialValue = null,
    ) {
        value = try {
            GetLevelDetails(level.level_id)
        } catch (e: Exception) {
            null
        }
    }.value

    Column() {
        val videoURL = levelDetails?.verifications[0]?.video_url
        if (videoURL != null) {
            AsyncImage(
                model = BuildYTWebImage(videoURL),
                contentDescription = "Youtube thumbnail video of ${level.name}"
            )
        }
        else {
            Text(
                text = "Image failed to load.",
                color = Color.White
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#${level.position} - ${level.name}",
                color = Color.White
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column() {
                Text(
                    text = "Published by: ${levelDetails?.publisher?.global_name}",
                    color = Color.White
                )
                Text(
                    text = "Verified by: ${levelDetails?.verifications[0]?.submitted_by?.global_name}",
                    color = Color.White
                )
            }
        }
    }
}
