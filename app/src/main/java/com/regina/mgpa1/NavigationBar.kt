package com.regina.mgpa1

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Image
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.regina.mgpa1.games.Higher_LowerGame
import kotlinx.coroutines.launch

//==CLASSES

//An enum class to store where each nav button needs to go to
enum class NavDestination(
    val route: String,
    val label: String,
    @param:DrawableRes val icon : Int,
    val contentDescription: String,
    val section: AppSection
){
    HOME("HOME", "Home", R.drawable.home_page_icon, "Home", Higher_LowerGame()),
    DEMON("DEMON", "Demon List", R.drawable.demon_page_icon, "Demon List", DemonAppSection()),
    GAMES("GAMES", "Games", R.drawable.games_page_icon, "Games", GamesAppSection()),
    LOGIN("LOGIN", "Login", R.drawable.login_page_icon, "Login", LoginAppSection())
}

//==FUNCTIONS
//This function aims to render the appropriate Navigation Content at the appropriate time.
@Composable
fun NavContentLoader(
    navController: NavHostController,
    navDestination: NavDestination,
    modifier : Modifier = Modifier
) {
    NavHost(navController, startDestination = navDestination.route) {
        //Store All Navigation related items here
        NavDestination.entries.forEach{destination ->
            //Create the appropriate composables for each screen
            composable(destination.route){
                destination.section.SectionLayout()
            }
        }
    }
}

//Renders the Navbar and the buttons within it
//This is a test func before I actually build the proper UI
//Regina: Edited this function to implement swipe navigation.
@Composable
fun NavigationBar(){
    val destinations = NavDestination.entries
    val pagerState = rememberPagerState(
        initialPage = NavDestination.HOME.ordinal,
        pageCount = { destinations.size }
    )
    val scope = rememberCoroutineScope()


    //Use this as the main UI Holder
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomAppBar{
                //NavBar - tie the Inset logic
                NavigationBar(windowInsets = NavigationBarDefaults.windowInsets){
                    destinations.forEachIndexed { index, destination ->
                        //Create new nav Buttons
                        NavigationBarItem(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            icon = {
                                //my SVG files REFUSE to cooperate so this will be empty for now
                                Image(
                                    painter = painterResource(id = destination.icon),
                                    contentDescription = destination.contentDescription
                                )
                            },
                            label = { Text(text = destination.label) }
                        )
                    }
                }
            }
        } //Lambda
    ) { innerPadding ->
        //Main Content. Note: The content (the list UI and allat shouldnt exist here at ALL. They
        //should be in their own classes which gets handled by the loader
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { page ->
            destinations[page].section.SectionLayout()
        }
    }
}
