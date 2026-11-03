package com.regina.mgpa1.games

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import coil.compose.AsyncImage
import com.regina.mgpa1.AppSection
import com.regina.mgpa1.GetLevelThumbnail
import com.regina.mgpa1.getRandomLevel
import com.regina.mgpa1.Level
import com.regina.mgpa1.R
import kotlinx.coroutines.launch

//==CLASSES

//A class that implements the Higher Lower game
class HigherLowerGame(
    val onGameOver: (hasWon: Boolean) -> Unit
) : AppSection(header = "Higher/Lower") {
    @Composable
    override fun SectionLayout() {
        val scope = rememberCoroutineScope()

        // Get the 2 random levels; need to use produceState() because
        // GetRandomLevel() can only be called in a coroutine
        val levels = produceState(
            initialValue = emptyList()
        ) {
            value = listOf(getRandomLevel(), getRandomLevel())
        }.value

        if (levels.isEmpty()) {
            RenderMessage(text = "Loading game...")
            return
        }

        // Should use rememberSaveable instead.
        var knownLevel by rememberSaveable() {
            mutableStateOf(value = levels[0])
        }

        // To revisit later and write our own Saver for this data class.
        var unknownLevel by rememberSaveable() {
            mutableStateOf(value = levels[1])
        }

        val knownPos = knownLevel.position
        val unknownPos = unknownLevel.position

        if (knownPos == null || unknownPos == null) {
            RenderMessage(text = "Game failed to load.")
            return
        }

        var UIUnknownPos by remember {
            mutableIntStateOf(value = -1)
        }

        var streak by rememberSaveable() {
            mutableIntStateOf(value = 0)
        }

        var shouldShowPanel by remember {
            mutableStateOf(value = false)
        }

        // Used to overlay the ChoiceButtons over everything else
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            //Base box
            Column(
                modifier = Modifier.fillMaxSize()
            ){
                //Modifier Values(I placed box mod here cus the weight var is only accesible in the column)
                val boxModifier: Modifier = Modifier
                    .weight(0.5f)
                    .background(Color("#2E2E5A".toColorInt()))
                    .border(
                        width = 2.dp,
                        color = Color("#252535".toColorInt())
                    )
                    .fillMaxWidth()

                //Stat tracker region
                Box(
                    modifier = Modifier
                        .weight(0.08f)
                        .background(Color("#1f2154".toColorInt()))
                        .fillMaxWidth()
                )
                {
                    Text(
                        text = "Streak: $streak",
                        fontSize = 30.sp
                    )
                }

                //Known Region
                KnownRegion(boxModifier, knownLevel, knownPos)

                //Unknown Region
                UnknownRegion(
                    boxModifier,
                    unknownLevel, UIUnknownPos,
                    onClick = {
                        shouldShowPanel = true
                    }
                )
            }

            if (shouldShowPanel) {
                ChoicePanel(
                    onChoiceMade = { choice ->
                        val isChoiceCorrect = evaluateChoice(
                            choice, unknownPos, knownPos
                        )

                        // Respond to the evaluation
                        if (isChoiceCorrect) {
                            // Reveal the position
                            //UIUnknownPos = unknownPos

                            // Go next round
                            knownLevel = unknownLevel
                            scope.launch {unknownLevel = getRandomLevel()}

                            // Increase streak
                            ++streak
                        }
                        else {
                            // Reveal the position
                            //UIUnknownPos = unknownPos

                            // Reset streak
                            streak = 0

                            // Go back to the overview page
                            onGameOver(false)
                        }

                        shouldShowPanel = false
                    }
                )
            }
        }
    }
}

//An enum class that defines the choices a user can make
enum class UserChoice() {
    NONE, HIGHER, LOWER
}

//==FUNCTIONS

//A Composable function that renders the known region
@Composable
fun ColumnScope.KnownRegion(modifier: Modifier, knownLevel: Level, knownPos: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ){
        // Loading the background image
        AsyncImage(
            model = GetLevelThumbnail(LevelID = knownLevel.level_id),
            contentDescription = "Thumbnail",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        LevelInfo(
            levelName = knownLevel.name,
            position = knownPos
        )
    }
}

//A Composable function that renders the unknown region
@Composable
fun ColumnScope.UnknownRegion(
    modifier: Modifier,
    unknownLevel: Level, UIUnknownPos: Int,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(size = 10.dp)
            )
            .clickable() {
                onClick()
            }
    ){
        // Loading the background image
        AsyncImage(
            model = GetLevelThumbnail(LevelID = unknownLevel.level_id),
            contentDescription = "Thumbnail",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        LevelInfo(
            levelName = unknownLevel.name,
            position = UIUnknownPos
        )
    }
}

//A Composable function that renders level information
@Composable
fun LevelInfo(levelName: String, position: Int) {
    val positionValue = if (position > 0) position.toString() else "???"

    Text(
        text = "$levelName #$positionValue",
        fontSize = 40.sp,
        lineHeight = 40.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .widthIn(max = 300.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(size = 10.dp)
            )
            .background(color = MaterialTheme.colorScheme.background)
            .padding(all = 10.dp)
    )
}

//A Composable function that presents the user with higher / lower choice buttons
@Composable
fun ChoicePanel(onChoiceMade: (UserChoice) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        // Higher button
        ChoiceButton(
            onButtonClick = {
                onChoiceMade(UserChoice.HIGHER)
            },
            painterID = R.drawable.keyboard_arrow_up,
            contentDescription = "Higher"
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Lower button
        ChoiceButton(
            onButtonClick = {
                onChoiceMade(UserChoice.LOWER)
            },
            painterID = R.drawable.keyboard_arrow_down,
            contentDescription = "Lower"
        )
    }
}

//A Composable function that defines the appearance of a choice button
@Composable
fun ChoiceButton(
    onButtonClick: () -> Unit, @DrawableRes painterID: Int, contentDescription: String
) {
    OutlinedIconButton(
        onClick = {
            onButtonClick()
        },
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.onBackground
        ),
        colors = IconButtonDefaults.outlinedIconButtonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        modifier = Modifier
            .fillMaxWidth(fraction = 0.2f)
            .height(85.dp)
    ) {
        Icon(
            painter = painterResource(id = painterID),
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(fraction = 0.75f)
        )
    }
}

//A function to show the user a message
@Composable
fun RenderMessage(text: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

//A function that evaluates whether the user's choice is correct
fun evaluateChoice(
    choice: UserChoice, unknownPos: Int, knownPos: Int
): Boolean {
    // Note: We are evaluating RANKINGS. Higher pos = smaller int
    return when (choice) {
        UserChoice.HIGHER -> (unknownPos < knownPos)
        UserChoice.LOWER -> (unknownPos > knownPos)
        else -> false
    }
}
