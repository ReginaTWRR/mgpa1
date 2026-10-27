package com.regina.mgpa1

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

//==CLASSES

//A class that implements the Home section of the App
class HomeAppSection : AppSection("Home") {
    @Composable
    override fun SectionLayout() {
        Column() {
//            Text(text = header)
            Button(
                onClick = {
                    //Gonna use this to pull data
                    println("hi")
                }
            ) {
                Text("button")
            }
        }
    }
}
