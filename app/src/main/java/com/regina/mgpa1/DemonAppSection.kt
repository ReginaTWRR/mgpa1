package com.regina.mgpa1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

//==CLASSES

//A class that implements the Demon section of the App
class DemonAppSection : AppSection("Demon List") {
    @Preview
    @Composable
    override fun SectionLayout() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = header,
                    modifier = Modifier
                )
            }
            DemonCard()
        }

        //

        val scope = rememberCoroutineScope()
        val LevelsList = produceState(initialValue = emptyList<Level>()) {value = GetLevelList() }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            item {
                //Hi!!! So, for now i made this into a lazy column and it just loads in a text. I
                //tried my best to optimise loading the list. Just saying, theres about 1k+ levels.
                //There are 2 options. A: Limit the list. B: Optimise the loading (somehow). C:Load once,
                //Store the info in memory(possible sideffects). You can try to explore your own solutions so yea
                //Good luck
                var fullList = StringBuffer()
                for((_, name) in LevelsList.value){
                    fullList.append(name + "\n")
                }
                Text(text = fullList.toString())
            }
        }
//        Column() {
//            Text(text = header)
//
//
//        }
    }
}

@Composable
fun DemonCard() {

}
