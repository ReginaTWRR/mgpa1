package com.regina.mgpa1.sections

import android.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.regina.mgpa1.logic.BuildYTWebImage
import com.regina.mgpa1.logic.ErrorHandler
import com.regina.mgpa1.logic.GetLevelDetails
import com.regina.mgpa1.logic.Level
import com.regina.mgpa1.logic.LevelDetails
import com.regina.mgpa1.logic.LevelName
import com.regina.mgpa1.viewModels.DemonListViewModel
import kotlin.math.exp

//==CLASSES

//A class that implements the Demon section of the App
class DemonAppSection : AppSection(header = "Demon List") {

    @Preview
    @Composable
    override fun SectionLayout() {
        // Access the view model and get the level list
        val viewModel = viewModel<DemonListViewModel>()
        val levelList by viewModel.levelList.collectAsState()
        if(levelList.isEmpty())
            return
        val ErrorLevel = levelList[0]
        if(ErrorLevel.id.equals("-1")){
            ErrorHandler(ErrorLevel)
            return
        }

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
//This is for checking if the demonList was loaded or not so that we can know when to expand the first card
var initialised by mutableStateOf(false)
//==FUNCTIONS

//A Composable function that renders all levels in the passed list
@Composable
fun RenderLevelList(levels: List<Level>) {
    var hi by rememberSaveable{mutableStateOf(false)}
    println("adsdgfdlfjgbh $hi")
    if(!hi){
        hi = true;
        println("hi")
    }
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
            if(level.position == 1){
                if(!initialised){
                    initialised = true;
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
fun LevelCard(level: Level,Expanded: Boolean = false,onClick:(expanded:Boolean)->Unit) {
    //Card Variables
    //This forcefully triggers recomposition. when the card is loaded, it gets updated to the accurate state.
    var expanded by remember{ mutableStateOf(level.expanded) }
//    val expanded = level.expanded;
//    if(level.position == 1){
//        println("1 $initialised")
//        if(!initialised){
//            initialised = true;
//            println("2 $initialised")
////            expanded = true;
//        }
//    }

    //==MODIFIERS
    val headerBackground = Modifier.background(Color.Transparent)
    val contentBackground = Modifier.background(Color.Transparent)
    val fakePaddingModifiers = Modifier.height(16.dp).fillMaxWidth().then(headerBackground)
    Card(modifier =  Modifier.fillMaxSize()) {
        Column(
//            modifier = Modifier.fillMaxWidth().clickable{expanded = !expanded}.then(headerBackground),
            modifier = Modifier.fillMaxWidth().clickable{onClick(!level.expanded);expanded = !expanded}.then(headerBackground),
            horizontalAlignment = Alignment.CenterHorizontally){
            Spacer(modifier = fakePaddingModifiers)
            //Render Level Name
            LevelName(level, modifier = Modifier)
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

                //Only grab the Details here and not when the entire func is called
                val levelDetails = produceState<LevelDetails?>(
                    initialValue = null,
                ){
                    value = try {
                        GetLevelDetails(LevelID = level.level_id)
                    } catch (e: Exception) {
                        null
                    }
                }.value
                //This column is here to organise the spacer and the card contents.
                //BackGround styling should be done in this Column
                Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    //So that it looks like the bottom padding(when closed) is here
                    Spacer(modifier = fakePaddingModifiers)
                    //Place Pre-Loaded UI here
                    if(levelDetails == null){
                        Text("loading Level...", textAlign = TextAlign.Center)
                    }
                    //Response code if id == -1.
                    else if(levelDetails.id == "-1"){
                        ErrorHandler(levelDetails)
                    }
                    else {
                        CardContent(levelDetails)
                    }
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
fun CardContent(levelDetails: LevelDetails?){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //This might break if theres no verification. Be carefull.
        // Apparently, one level can be verified multiple times. For the purpose of
        // this UI, we will just use the first one.
        val videoURL = levelDetails?.verifications[0]?.video_url

        if (videoURL != null) {
            AsyncImage(
                model = BuildYTWebImage(YTLink = videoURL),
                contentDescription = "Youtube thumbnail video of ${levelDetails.name}"
            )
        } else {
            Text(text = "Image failed to load.")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            Column() {
                Text(text = "Published by: ${levelDetails?.publisher?.global_name}")
                Text(text = "Verified by: ${levelDetails?.verifications[0]?.submitted_by?.global_name}")
            }
        }
    }
}
