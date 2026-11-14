package com.regina.mgpa1.logic

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
import com.regina.mgpa1.R
import com.regina.mgpa1.sections.AppSection
import com.regina.mgpa1.sections.DemonAppSection
import com.regina.mgpa1.sections.GamesAppSection
import com.regina.mgpa1.sections.HomeAppSection
import com.regina.mgpa1.sections.LoginAppSection
import kotlinx.coroutines.launch

//==CLASSES

//An enum class to store where each nav button needs to go to
enum class NavDestination(
    val route: String,
    val label: String,
    @param:DrawableRes val icon : Int,
    val contentDescription: String
){
    HOME(
        route = "HOME",
        label = "Home",
        icon = R.drawable.home_page_icon,
        contentDescription = "Home"
    ),
    DEMON(
        route = "DEMON",
        label = "Demon List",
        icon = R.drawable.demon_page_icon,
        contentDescription = "Demon List"
    ),
    GAMES(
        route = "GAMES",
        label = "Games",
        icon = R.drawable.games_page_icon,
        contentDescription = "Games"
    ),
    LOGIN(
        route = "LOGIN",
        label = "Login",
        icon = R.drawable.login_page_icon,
        contentDescription = "Login"
    )
}

// A data class to group a NavDestination and AppSection together
data class NavSection(
    val destination: NavDestination,
    val section: AppSection
)

// A class to group functions related to section navigation
class SectionNavigation {
    //==FUNCTIONS

    //Renders the Navbar and the buttons within it
    //This is a test func before I actually build the proper UI
    //Regina: Edited this function to implement swipe navigation.
    @Composable
    fun NavigationBar() {
        val destinations = NavDestination.entries

        val pagerState = rememberPagerState(
            initialPage = NavDestination.HOME.ordinal,
            pageCount = { destinations.size }
        )

        val scope = rememberCoroutineScope()

        // Lambda function to request for a change in the section
        val requestSectionChange: (newSection: NavDestination) -> Unit = { newSection ->
            // Check if we are already on this section
            if (pagerState.currentPage != newSection.ordinal) {
                scope.launch {
                    pagerState.animateScrollToPage(newSection.ordinal)
                }
            }
        }

        // List of nav sections
        val navSections = listOf(
            NavSection(
                destination = NavDestination.HOME,
                section = HomeAppSection(requestSectionChange)
            ),
            NavSection(
                destination = NavDestination.DEMON,
                section = DemonAppSection()
            ),
            NavSection(
                destination = NavDestination.GAMES,
                section = GamesAppSection()
            ),
            NavSection(
                destination = NavDestination.LOGIN,
                section = LoginAppSection()
            )
        )

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
                    .padding(paddingValues = innerPadding)
            ) { section ->
                navSections[section].section.SectionLayout()
            }
        }
    }
}
