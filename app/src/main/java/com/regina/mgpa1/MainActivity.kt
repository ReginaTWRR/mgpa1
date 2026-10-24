package com.regina.mgpa1

import android.R.drawable
import android.credentials.CreateCredentialException
import android.graphics.Paint
import android.graphics.drawable.Icon
import android.graphics.pdf.content.PdfPageGotoLinkContent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.contentType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.regina.mgpa1.ui.theme.Mgpa1Theme
import kotlin.coroutines.coroutineContext

//==CLASSES
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Mgpa1Theme {
                //Use this as the main UI Holder
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {NavBarTest()} //Lambda
                ){ innerPadding ->
                    //Main Content
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}




//==FUNCTIONS
@Composable
fun Greeting( name:String, modifier: Modifier){

}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Mgpa1Theme {
        val navController = rememberNavController()
        val startDest = NavDestination.TEST1
        var selectedIndex by rememberSaveable { mutableIntStateOf(startDest.ordinal) }

//        Greeting("Android")
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {BottomAppBar() {
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
            }} //Lambda
        ){ innerPadding ->
            //Main Content
//            Greeting(
//                name = "Android",
//                modifier = Modifier.padding(innerPadding)
//            )
            NavContentLoader(navController,startDest, modifier = Modifier.padding(innerPadding))
        }
    }
}