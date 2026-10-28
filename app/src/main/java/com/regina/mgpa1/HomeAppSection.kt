package com.regina.mgpa1

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//==CLASSES

//A class that implements the Home section of the App
class HomeAppSection : AppSection("Home") {
    @Composable
    override fun SectionLayout() {
        val scope = rememberCoroutineScope()
        Column() {
//            Text(text = header)
            Button(
                onClick = {
                    //Gonna use this to pull data
                    scope.launch {
//                        TestGet()
//                        GetfromAPI("/api/aredl/levels")
                        GetLevelList()
                    }

                    println("hi")
                }
            ) {
                Text("button")
            }
        }
    }
}
