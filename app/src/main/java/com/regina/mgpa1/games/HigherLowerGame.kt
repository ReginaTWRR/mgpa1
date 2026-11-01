package com.regina.mgpa1.games

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.regina.mgpa1.AppSection
import com.regina.mgpa1.GetLevelThumbnail
import com.regina.mgpa1.GetRandomLevel
import com.regina.mgpa1.Level
import com.regina.mgpa1.R

//==CLASSES

//A class that implements the Higher Lower game
class HigherLowerGame : AppSection("Higher/Lower"){
    @Preview
    @Composable
    override fun SectionLayout() {
        // Get the 2 random levels; need to use produceState() because
        // GetRandomLevel() can only be called in a coroutine
        val levels = produceState(
            initialValue = emptyList()
        ) {
            value = listOf(GetRandomLevel(), GetRandomLevel())
        }.value

        if (levels.isNotEmpty())
        {
            val knownLevel = levels[0]
            val unknownLevel = levels[1]

            var shouldShowPanel by remember {
                mutableStateOf(value = false)
            }

            var streak by remember {
                mutableIntStateOf(value = 0)
            }

            // Used to overlay the ChoiceButtons over everything else
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                //Base box
                Column(
                    modifier = Modifier.fillMaxSize()
                ){
                    //Stat tracker region
                    Box(
                        modifier = Modifier
                            .weight(0.1f)
                            .background(Color.Blue)
                            .fillMaxWidth()
                    )
                    {
                        Text(
                            text = "Streak: $streak",
                            fontSize = 30.sp
                        )
                    }

                    //Known Region
                    LevelOption(
                        knownLevel,
                        onClick = {
                            shouldShowPanel = true
                        }
                    )

                    //Unknown Region
                    LevelOption(
                        unknownLevel,
                        onClick = {
                            shouldShowPanel = true
                        },
                        isPositionKnown = false
                    )
                }

                if (shouldShowPanel)
                    ChoicePanel()
            }
        }
        else
        {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Loading game...",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

//==FUNCTIONS

//A Composable function that renders a level option. Need to pass ColumnScope to
// the function because .weight() can only work in a Column or Row
@Composable
fun ColumnScope.LevelOption(
    level: Level, onClick: () -> Unit, isPositionKnown: Boolean = true
) {
    Box(
        modifier = Modifier
            .weight(0.5f)
            .background(Color.Red)
            .fillMaxWidth()
            .clickable() {
                onClick()
            }
    ){
        // Loading the background image
        AsyncImage(
            model = GetLevelThumbnail(LevelID = level.level_id),
            contentDescription = "Thumbnail",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = level.name,
                fontSize = 40.sp,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background
                    )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = if (isPositionKnown) "#${level.position}" else "#???",
                fontSize = 40.sp,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background
                    )
            )
        }
    }
}

//A Composable function that presents the user with higher / lower choice buttons
@Composable
fun ChoicePanel() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        // Higher button
        ChoiceButton(
            painterID = R.drawable.keyboard_arrow_up,
            contentDescription = "Higher"
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Lower button
        ChoiceButton(
            painterID = R.drawable.keyboard_arrow_down,
            contentDescription = "Lower"
        )
    }
}

//A Composable function that defines the appearance of a choice button
@Composable
fun ChoiceButton(@DrawableRes painterID: Int, contentDescription: String) {
    FilledIconButton(
        onClick = {

        },
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        modifier = Modifier
            .fillMaxWidth(fraction = 0.5f)
            .height(200.dp)
    ) {
        Icon(
            painter = painterResource(id = painterID),
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(fraction = 0.75f)
        )
    }
}
