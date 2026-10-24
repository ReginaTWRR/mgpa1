package com.regina.mgpa1

import androidx.annotation.DrawableRes
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

//==CLASSES

//An enum class to store where each nav button needs to go to
enum class NavDestination(
    val route: String,
    val label: String,
    @param:DrawableRes val icon : Int,
    val contentDescription: String
){
    TEST1("TEST1","TEST1",R.drawable.ic_launcher_background,"i"),
    TEST2("TEST2","TEST2",R.drawable.ic_launcher_foreground,"i"),
    TEST3("TEST3","TEST3",android.R.drawable.star_on,"i"),
    TEST4("TEST4","TEST4",android.R.drawable.star_off,"i"),
}


//==FUNCTIONS
//This function aims to render the appropriate Navigation Content at the appropriate time.
@Composable
fun NavContentLoader(navController: NavHostController,_navdestination: NavDestination,modifier : Modifier = Modifier){
    NavHost(navController, startDestination = _navdestination.route) {
        NavDestination.entries.forEach{destination ->
            composable(destination.route){
                when(destination){
                    NavDestination.TEST1 -> println("poo1");
                    NavDestination.TEST2 -> println("poo2");
                    NavDestination.TEST3 -> println("poo3");
                    NavDestination.TEST4 -> println("poo4");
                }
            }
        }
    }
}

//Renders the Navbar and the buttons within it
//This is a test func before I actually build the proper UI
@Composable
fun NavBarTest(){
    val navController = rememberNavController()
    val startDest = NavDestination.TEST1
    var selectedIndex by rememberSaveable { mutableIntStateOf(startDest.ordinal) }

    return BottomAppBar() {
        NavigationBar(
            windowInsets = NavigationBarDefaults.windowInsets
        ) {
            NavDestination.entries.forEachIndexed { index, destination ->
                //Create new nav Buttons
                NavigationBarItem(
                    selected = index == selectedIndex,
                    onClick = {
                        navController.navigate(route = destination.route)
                        selectedIndex = index
                    },
                    icon = {
                    },
                    label = { Text(text = destination.label) }
                )
            }
        }
    }
}
