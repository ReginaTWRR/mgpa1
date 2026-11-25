package com.regina.mgpa1.games.higherLower

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.regina.mgpa1.ErrorUIs.preLoadedUI
import com.regina.mgpa1.sections.AppSection
import com.regina.mgpa1.logic.GetLevelThumbnail
import com.regina.mgpa1.logic.Level
import com.regina.mgpa1.R
import com.regina.mgpa1.logic.BrushTitle
import com.regina.mgpa1.logic.OutlinedHeadline
import com.regina.mgpa1.logic.RenderMessage
import com.regina.mgpa1.logic.StandardTitle
import com.regina.mgpa1.logic.ErrorHandler
import com.regina.mgpa1.viewModels.DemonListViewModel

//==CLASSES

// A class that implements the main Higher Lower game
class MainHigherLowerGame (
    val onGameExit: () -> Unit,
    val returnOverviewPage: () -> Unit
) : AppSection(header = "Main Higher/Lower") {
    @Composable
    override fun SectionLayout() {
        BackHandler {
            // Go back to the Games section
            returnOverviewPage()
        }
        // Access the view model and get the 2 random levels
        val viewModel = viewModel<DemonListViewModel>()
        val levelList by viewModel.levelList.collectAsState()

        if(levelList.isEmpty()) return

        val errorLevel = levelList[0]

        if(errorLevel.id == "-1") {
            //Center both button and Error UI.
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                ErrorHandler(errorLevel)
                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = returnOverviewPage) { Text("return to Games Page") }
            }

            return
        }

        var knownLevel by rememberSaveable {
            mutableStateOf<Level?>(value = null)
        }
        var unknownLevel by rememberSaveable {
            mutableStateOf<Level?>(value = null)
        }

        // LaunchedEffect: This block of code runs when levelList changes
        // (i.e. Finishes loading)
        LaunchedEffect(key1 = levelList) {
            if (levelList.size >= 2) {
                if (knownLevel == null || unknownLevel == null) {
                    knownLevel = levelList.random()
                    unknownLevel = levelList.random()
                }
            }
        }

        // Need val versions of the known and unknown levels to allow for smart-casting
        val currentKnownLevel = knownLevel
        val currentUnknownLevel = unknownLevel

        if (currentKnownLevel == null || currentUnknownLevel == null) {
            preLoadedUI()
            return
        }

        val knownPos = currentKnownLevel.position
        val unknownPos = currentUnknownLevel.position

        if (knownPos == null || unknownPos == null) {
            RenderMessage(
                text = "Game failed to load",
                modifier = Modifier.fillMaxWidth()
            )
            return
        }

        var uiUnknownPos by remember {
            mutableIntStateOf(value = -1)
        }

        var streak by rememberSaveable {
            mutableIntStateOf(value = 0)
        }

        var shouldShowPanel by remember {
            mutableStateOf(value = false)
        }

        BackHandler {
            // Go back to the Games section
            returnOverviewPage()
        }

        // Used to overlay the ChoiceButtons over everything else
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            //Base box
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
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
                ) {
                    Text(
                        text = "Streak: $streak",
                        fontSize = 30.sp,
                        modifier = Modifier.padding(all = 10.dp)
                    )
                }

                //Known Region
                KnownRegion(boxModifier, currentKnownLevel, knownPos)

                //Unknown Region
                UnknownRegion(
                    boxModifier,
                    currentUnknownLevel, uiUnknownPos,
                    onClick = {
                        shouldShowPanel = true
                    }
                )
            }

            if (shouldShowPanel) {
                ChoicePanel(
                    unknownLevel = currentUnknownLevel, knownLevel = currentKnownLevel,
                    onChoiceMade = { choice ->
                        shouldShowPanel = false

                        if (choice == UserChoice.NONE) return@ChoicePanel

                        val isChoiceCorrect = evaluateChoice(
                            choice, unknownPos, knownPos
                        )

                        // Respond to the evaluation
                        if (isChoiceCorrect) {
                            // Go next round
                            knownLevel = unknownLevel
                            unknownLevel = levelList.randomOrNull()

                            // Increase streak
                            ++streak
                        } else {
                            // Reset streak
                            streak = 0

                            // Go back to the game lost page
                            onGameExit()
                        }
                    }
                )
            }
        }
    }
}

// An enum class that defines the choices a user can make
enum class UserChoice {
    NONE, HIGHER, LOWER
}

//==FUNCTIONS

// A Composable function that renders the known region
@Composable
fun KnownRegion(modifier: Modifier, knownLevel: Level, knownPos: Int) {
    var imageLoading by remember { mutableStateOf(false) } //Check if we need to load preloaded or image

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        // Loading the background image - Shld place error images here
        AsyncImage(
            model = GetLevelThumbnail(LevelID = knownLevel.level_id),
            contentDescription = "Thumbnail",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
            onLoading = {imageLoading = true},
            onError =  {imageLoading = false},
            onSuccess =  {imageLoading = false}
        )
        if(imageLoading){
            preLoadedUI()
            return //Dont load the text until the image has loaded as well
        }
        GameLevelInfo(
            levelName = knownLevel.name,
            position = knownPos
        )
    }
}

// A Composable function that renders the unknown region
@Composable
fun UnknownRegion(
    modifier: Modifier,
    unknownLevel: Level, uiUnknownPos: Int,
    onClick: () -> Unit
) {
    var imageLoading by remember { mutableStateOf(false) } //Check if we need to load preloaded or image
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(size = 10.dp)
            )
            .clickable {
                //Only allow input when images are done loading
                if(!imageLoading){
                    onClick()
                }
            }
    ) {
        // Loading the background image
        AsyncImage(
            model = GetLevelThumbnail(LevelID = unknownLevel.level_id),
            contentDescription = "Thumbnail",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
            onLoading = {imageLoading = true},
            onError =  {imageLoading = false},
            onSuccess =  {imageLoading = false}
        )
        if(imageLoading){
            preLoadedUI()
            return
        }
        GameLevelInfo(
            levelName = unknownLevel.name,
            position = uiUnknownPos
        )
    }
}

// A Composable function that renders level information
@Composable
fun GameLevelInfo(levelName: String, position: Int) {
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

// A Composable function that presents the user with higher / lower choice buttons
@Composable
fun ChoicePanel(
    unknownLevel: Level, knownLevel: Level, onChoiceMade: (UserChoice) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize(fraction = 0.75f)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(size = 10.dp)
            )
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        val unknownGradient = listOf(
            MaterialTheme.colorScheme.onSecondary,
            MaterialTheme.colorScheme.onBackground
        )
        val knownGradient = listOf(Color.Gray, Color.White)

        BackHandler {
            onChoiceMade(UserChoice.NONE)
        }

        Box(
            modifier = Modifier.weight(1f)
        ) {
            // Exit button
            IconButton(
                onClick = {
                    onChoiceMade(UserChoice.NONE)
                },
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "Close Button"
                )
            }

            // Question text
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                FlowRow {
                    StandardTitle(text = "Is")
                    Spacer(modifier = Modifier.width(5.dp))
                    BrushTitle(text = unknownLevel.name, colors = unknownGradient)
                }
                Spacer(modifier = Modifier.height(5.dp))

                OutlinedHeadline(
                    text = "HIGHER or LOWER",
                    outlinedColor = MaterialTheme.colorScheme.onSecondary,
                    fillColor = MaterialTheme.colorScheme.onBackground,
                    outlineWidth = 2.dp
                )
                Spacer(modifier = Modifier.height(5.dp))

                FlowRow {
                    StandardTitle(text = "than")
                    Spacer(modifier = Modifier.width(5.dp))
                    BrushTitle(text = knownLevel.name, colors = knownGradient)
                    StandardTitle(text = "?")
                }
            }
        }

        // Choice buttons
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
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
}

// A Composable function that defines the appearance of a choice button
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
        modifier = Modifier.size(112.5.dp)
    ) {
        Icon(
            painter = painterResource(id = painterID),
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(fraction = 0.75f)
        )
    }
}

// A function that evaluates whether the user's choice is correct
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
