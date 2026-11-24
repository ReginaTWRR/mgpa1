package com.regina.mgpa1.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.regina.mgpa1.logic.BuildYTWebImage
import com.regina.mgpa1.logic.GetLevelDetails
import com.regina.mgpa1.logic.Level
import com.regina.mgpa1.logic.LevelDetails
import com.regina.mgpa1.logic.LevelName
import com.regina.mgpa1.viewModels.DemonListViewModel

//==CLASSES

//A class that implements the Demon section of the App
class DemonAppSection : AppSection(header = "Demon List") {
    @Preview
    @Composable
    override fun SectionLayout() {
        // Access the view model and get the level list
        val viewModel = viewModel<DemonListViewModel>()
        val levelList by viewModel.levelList.collectAsState()

        // Biggest rendering body
        Column(
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

            // Render the list
            if (levelList.isNotEmpty())
            {
                RenderLevelList(levels = levelList)
            }
            else
            {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Loading levels...",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

//==FUNCTIONS

//A Composable function that renders all levels in the passed list
@Composable
fun RenderLevelList(levels: List<Level>) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground
            )
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
            GetLevelDetails(LevelID = level.level_id)
        } catch (e: Exception) {
            null
        }
    }.value

    Column() {
        // Apparently, one level can be verified multiple times. For the purpose of
        // this UI, we will just use the first one.
        val videoURL = levelDetails?.verifications[0]?.video_url

        if (videoURL != null) {
            AsyncImage(
                model = BuildYTWebImage(YTLink = videoURL),
                contentDescription = "Youtube thumbnail video of ${level.name}"
            )
        }
        else {
            Text(text = "Image failed to load.")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            LevelName(level, modifier = Modifier)
            Spacer(modifier = Modifier.width(10.dp))

            Column() {
                Text(text = "Published by: ${levelDetails?.publisher?.global_name}")
                Text(text = "Verified by: ${levelDetails?.verifications[0]?.submitted_by?.global_name}")
            }
        }
    }
}
