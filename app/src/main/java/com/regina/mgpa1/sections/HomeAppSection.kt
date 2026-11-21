package com.regina.mgpa1.sections

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.regina.mgpa1.logic.GetLevelThumbnail
import com.regina.mgpa1.R
import com.regina.mgpa1.logic.ErrorHandler
import com.regina.mgpa1.logic.GetLevelDetails
import com.regina.mgpa1.logic.Level
import com.regina.mgpa1.logic.LevelDetails
import com.regina.mgpa1.logic.LevelName
import com.regina.mgpa1.logic.RenderMessage
import com.regina.mgpa1.viewModels.DemonListViewModel
import com.regina.mgpa1.viewModels.DemonSearchViewModel
import com.regina.mgpa1.viewModels.HomePageViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.milliseconds

//==CLASSES

//A class that implements the Home section of the App
class HomeAppSection(
    val requestSectionChange: (newSection: NavDestination) -> Unit
) : AppSection(header = "Home") {
    @Composable
    override fun SectionLayout() {
        // Here, access the DemonListViewModel and pass it into the DemonSearchViewModel
        // using the custom factory function that we have defined in
        // the DemonSearchViewModel. This helps the app to not crash.
        val listViewModel = viewModel<DemonListViewModel>()

        //For lack of a better fix, this ensures that the search viewmodel and hompageview modek
        //Doesnt get inititalised until the list is properly found
        val ChecklevelList by listViewModel.levelList.collectAsState()
        if(ChecklevelList.isEmpty())
            return
        val ErrorLevel = ChecklevelList[0]
        if(ErrorLevel.id.equals("-1")){
            ErrorHandler(ErrorLevel)
            return
        }

        val searchViewModel = viewModel<DemonSearchViewModel>(
            factory = DemonSearchViewModel.factory(
                listViewModel = listViewModel
            )
        )
        val homeViewModel = viewModel<HomePageViewModel>(
            factory = HomePageViewModel.factory(
                listViewModel = listViewModel
            )
        )

        val searchText = searchViewModel.searchText.collectAsState().value
        val isKeyboardActive = searchViewModel.isKeyboardActive.collectAsState().value
        val levelList by searchViewModel.levelList.collectAsState(initial = emptyList())

        // BackHandler
        var backPressed by remember { mutableStateOf(value = false) }

        //Create a thread to reset the backpressed after 2s
        LaunchedEffect(backPressed) {
            if(backPressed){
                delay(2000.milliseconds)
                backPressed = false
            }
        }
        //Grab the local context
        val context = LocalContext.current
        val currActivity = LocalActivity.current

        BackHandler() {
            if(backPressed){
                currActivity?.finish()
            }
            else{
                backPressed = true
                Toast.makeText(context,"Tap again to exit", Toast.LENGTH_SHORT).show()
            }
        }

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
                SearchBar(searchViewModel, searchText)

                if (isKeyboardActive) {
                    SearchBarResults(
                        levelList = levelList,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.height(10.dp))
                    RandomDemon(homeViewModel)
                    Spacer(modifier = Modifier.height(10.dp))
                    DailyChallenge(requestSectionChange)
                }
            }
        }
    }
}

//==FUNCTIONS

//A Composable function that renders the search bar
@Composable
fun SearchBar(viewModel: DemonSearchViewModel, searchText: String) {
    // To unfocus the text field when the keyboard is down
    val focusManager = LocalFocusManager.current

    // Can only be called in the context of a Composable
    val isKeyboardActive = WindowInsets.ime.getBottom(density = LocalDensity.current) > 0
    var wasKeyboardActive by rememberSaveable() {
        mutableStateOf(value = false)
    }

    // This block executes when isKeyboardActive changes
    LaunchedEffect(key1 = isKeyboardActive) {
        // Update the viewModel's boolean
        viewModel.onKeyboardActivityChanged(isKeyboardActive)

        // Check if the user stopped searching. If so, clear focus
        if (!isKeyboardActive && wasKeyboardActive) {
            focusManager.clearFocus()
        }

        // Update wasSearching
        wasKeyboardActive = isKeyboardActive
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
            value = searchText,
            onValueChange = viewModel::onSearchTextChanged,
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

//A Composable function that renders the search bar results
@Composable
fun SearchBarResults(levelList: List<Level>, modifier: Modifier) {
    LazyColumn(
        modifier = modifier
    ) {
        items(levelList) { level ->
            LevelName(
                level = level,
                textColor = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

//A Composable function that renders a random demon
@Composable
fun RandomDemon(homeViewModel: HomePageViewModel) {
    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current

    // Access the view model and get the generated demon
    // Need to collect the level list as a state so that this recomposes when the list
    // is done loading in DemonListViewModel. Else, UI will not be updated when the list
    // actually loads.
    val generatedDemon by homeViewModel.generatedDemon.collectAsState()
    val demon = generatedDemon
    val LevelDetails: LevelDetails

    if (demon == null) {
        RenderMessage(
            text = "Loading demon...",
            modifier = Modifier.fillMaxWidth()
        )
        return
    }
    //Place this here to ensure demon != null
    runBlocking {
        LevelDetails = GetLevelDetails(demon.level_id)
    }

    //Response code check for levelDetails
    if(LevelDetails.id == "-1"){
        ErrorHandler(LevelDetails)
        return;
    }

    Column (
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Check out this demon!",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(10.dp))
        AsyncImage(
            model = GetLevelThumbnail(LevelID = demon.level_id),
            contentDescription = "${demon.name} thumbnail",
            error = painterResource(android.R.drawable.ic_dialog_alert)
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            LevelName(
                level = demon,
                textColor = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    // Go to the verification (gameplay) Youtube video
                    scope.launch {
                        val levelDetails = GetLevelDetails(LevelID = demon.level_id)
                        val videoURL = levelDetails.verifications[0].video_url
                        uriHandler.openUri(videoURL)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.background
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "WATCH GAMEPLAY")
                Spacer(modifier = Modifier.weight(1f))
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
fun DailyChallenge(requestSectionChange: (NavDestination) -> Unit) {
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
                    // Go to the Games section
                    requestSectionChange(NavDestination.GAMES)
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
