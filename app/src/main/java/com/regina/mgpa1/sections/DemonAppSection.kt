package com.regina.mgpa1.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.regina.mgpa1.ErrorUIs.preLoadedUI
import com.regina.mgpa1.logic.BuildYTWebImage
import com.regina.mgpa1.logic.Creator
import com.regina.mgpa1.logic.ErrorHandler
import com.regina.mgpa1.logic.GetLevelCreators
import com.regina.mgpa1.logic.GetLevelDetails
import com.regina.mgpa1.logic.Level
import com.regina.mgpa1.logic.LevelDetails
import com.regina.mgpa1.logic.LevelName
import com.regina.mgpa1.viewModels.DemonListViewModel
import kotlinx.coroutines.launch

//==CLASSES

//A class that implements the Demon section of the App
class DemonAppSection : AppSection(header = "Demon List") {
    @Preview
    @Composable
    override fun SectionLayout() {
        // Access the view model and get the level list
        val viewModel = viewModel<DemonListViewModel>()
        val levelList by viewModel.levelList.collectAsState()

        if (levelList.isEmpty()) {
            preLoadedUI()
            return
        }

        val errorLevel = levelList[0]
        if (errorLevel.id == "-1") {
            ErrorHandler(errorLevel)
            return
        }

        // Biggest rendering body
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
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
                    preLoadedUI()
                }
            }
        }
    }
}
//This is for checking if the demonList was loaded or not so that we can know when to expand the first card
var initialised by mutableStateOf(false)
//==FUNCTIONS

//A Composable function that renders all levels in the passed list
@Composable
fun RenderLevelList(levels: List<Level>) {
    var hi by rememberSaveable{mutableStateOf(false)}
    println("adsdgfdlfjgbh $hi")
    if(!hi){
        hi = true
        println("hi")
    }
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .border(width = 1.dp, color = MaterialTheme.colorScheme.onBackground)
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
            if(level.position == 1){
                if(!initialised){
                    initialised = true
                    println("WE CAN OVERCOME THEIR POWER~~~")
                   level.expanded = true
                }
            }
            LevelCard(level, onClick = {
                expanded->
                level.expanded = expanded
            })
        }
    }
}

//A Composable function that defines how we show the level details
@Composable
fun LevelCard(level: Level, onClick:(expanded:Boolean)->Unit) {
    //Card Variables
    //This forcefully triggers recomposition. when the card is loaded, it gets updated to the accurate state.
    var expanded by remember{ mutableStateOf(level.expanded) }

    //==MODIFIERS
    val headerBackground = Modifier.background(Color.Transparent)
    val contentBackground = headerBackground
    val textColor = MaterialTheme.colorScheme.inverseSurface

    val fakePaddingModifiers = Modifier.height(16.dp).fillMaxWidth().then(headerBackground)

    Card(modifier =  Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable{
                    onClick(!level.expanded);expanded = !expanded
                }.then(other = headerBackground),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = fakePaddingModifiers)

            //Render Level Name
            LevelName(level, textColor, modifier = Modifier)

            AnimatedVisibility(
                visible = expanded,
                Modifier.fillMaxWidth()
                    .then(
                        if(expanded){
                            Modifier.animateContentSize()
                        }
                        else{
                            Modifier
                        }
                    ).then(contentBackground)
            ){
                //This column is here to organise the spacer and the card contents.
                //BackGround styling should be done in this Column
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    //So that it looks like the bottom padding(when closed) is here
                    Spacer(modifier = fakePaddingModifiers)
                    CardContent(expandedLevel = level, textColor = textColor)
                }
            }
            //get rid of ugly spacing if its expanded
            //(Put in animatedVis to have it smoothly grow/Shrink along side card details)
            AnimatedVisibility(
                visible = !expanded,
                Modifier.fillMaxWidth()
            ){
                Spacer(modifier = fakePaddingModifiers)
            }
        }
    }
}

@Composable
fun CardContent(expandedLevel: Level, textColor: Color) {
    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current

    // Get level details
    val levelDetails = produceState<LevelDetails?>(
        initialValue = null,
    ) {
        value = try {
            GetLevelDetails(LevelID = expandedLevel.level_id)
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }.value

    // Verify level details
    //Place Pre-Loaded UI here
    if(levelDetails == null){
        preLoadedUI()
        return
    }
    //Response code if id == -1.
    else if(levelDetails.id == "-1"){
        ErrorHandler(levelDetails)
        return
    }

    // Get level creators
    val levelCreators = produceState<List<Creator>?>(
        initialValue = emptyList()
    ) {
        value = try {
            GetLevelCreators(LevelID = expandedLevel.level_id)
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }.value

    // Verify level creators
    //Place Pre-Loaded UI here
    if(levelCreators == null){
        preLoadedUI()
        return
    }
    //Response code if id == -1.
    for ((id) in levelCreators) {
        if(id == "-1"){
            ErrorHandler(ErrorDetails = levelDetails)
            return
        }
    }

    var description = expandedLevel.description

    if (description == null) {
        description = "This level does not have a description yet."
    }

    val tags = expandedLevel.tags
    val levelID = expandedLevel.level_id
    val points = expandedLevel.points
    val edelEnjoyment = expandedLevel.edel_enjoyment
    val song = expandedLevel.song
    val nlwTier = expandedLevel.nlw_tier
    val gddlTier = expandedLevel.gddl_tier

    val verification = levelDetails.verifications[0]
    val videoURL = verification.video_url
    val publisher = levelDetails.publisher.global_name
    val verifier = verification.submitted_by.global_name
    var imageLoading by remember { mutableStateOf(false) } //Check if we need to load preloaded or image

    HorizontalDivider(color = textColor)
    Spacer(modifier = Modifier.height(10.dp))

    // Description
    Text(text = description, color = textColor, textAlign = TextAlign.Center)
    Spacer(modifier = Modifier.height(10.dp))

    if(imageLoading){
        preLoadedUI()
    }
    // Thumbnail of gameplay video
    //This might break if theres no verification. Be carefull.
    // Apparently, one level can be verified multiple times. For the purpose of
    // this UI, we will just use the first one.
    AsyncImage(
        model = BuildYTWebImage(YTLink = videoURL),
        contentDescription = "Youtube thumbnail video of ${levelDetails.name}",
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxWidth()
            .clickable{
                // Go to the verification (gameplay) Youtube video
                scope.launch {
                    uriHandler.openUri(videoURL)
                }
            },
        onLoading = {imageLoading = true},
        onError =  {imageLoading = false},
        onSuccess =  {imageLoading = false}
    )
    Spacer(modifier = Modifier.height(10.dp))

    // Tags
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        tags.forEach { tag ->
            if (tag == null) return@forEach

            Row(
                modifier = Modifier.border(
                    width = 1.dp,
                    color = textColor,
                    shape = RoundedCornerShape(size = 10.dp)
                )
            ) {
                Text(
                    text = tag,
                    color = textColor,
                    modifier = Modifier.padding(all = 5.dp)
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))

    Row {
        // Level ID
        DescriptionLevelInfo(
            infoLabel = "LEVEL ID",
            infoValue = "$levelID",
            valueColor = textColor
        )

        // List points
        DescriptionLevelInfo(
            infoLabel = "LIST POINTS",
            infoValue = "$points",
            valueColor = textColor
        )

        // Edel enjoyment
        DescriptionLevelInfo(
            infoLabel = "EDEL ENJOYMENT",
            infoValue = "$edelEnjoyment",
            valueColor = textColor
        )
    }
    Spacer(modifier = Modifier.width(10.dp))

    Row {
        // Song
        DescriptionLevelInfo(
            infoLabel = "SONG",
            infoValue = "$song",
            valueColor = textColor
        )

        // NLW tier
        DescriptionLevelInfo(
            infoLabel = "NLW TIER",
            infoValue = "$nlwTier",
            valueColor = textColor
        )

        // GDDL tier
        DescriptionLevelInfo(
            infoLabel = "GDDL TIER",
            infoValue = "$gddlTier",
            valueColor = textColor
        )
    }
    Spacer(modifier = Modifier.width(10.dp))

    Column(
        modifier = Modifier.padding(start = 5.dp)
    ) {
        // Publisher
        Row {
            DescriptionLabel(text = "PUBLISHER")
            Spacer(modifier = Modifier.width(5.dp))
            DescriptionValue(text = publisher, textColor = textColor)
        }
        Spacer(modifier = Modifier.height(5.dp))

        // Verifier
        Row {
            DescriptionLabel(text = "VERIFIER")
            Spacer(modifier = Modifier.width(5.dp))
            DescriptionValue(text = verifier, textColor = textColor)
        }
        Spacer(modifier = Modifier.height(5.dp))

        // Creators
        Row {
            DescriptionLabel(text = "CREATORS")
            Spacer(modifier = Modifier.width(5.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                levelCreators.forEach { creator ->
                    DescriptionValue(text = creator.global_name, textColor = textColor)
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))

    HorizontalDivider(color = textColor)
}

// A function to render a level info (e.g. Level ID, song, etc.)
@Composable
fun RowScope.DescriptionLevelInfo(
    infoLabel: String, infoValue: String, valueColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)
    ) {
        DescriptionLabel(text = infoLabel)
        Spacer(modifier = Modifier.height(5.dp))
        DescriptionValue(text = infoValue, textColor = valueColor)
    }
}

// A function to render a description label
@Composable
fun DescriptionLabel(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.labelSmall
    )
}

// A function to render a description value
@Composable
fun DescriptionValue(text: String, textColor: Color) {
    Text(
        text = text,
        color = textColor,
        style = MaterialTheme.typography.labelSmall
    )
}
