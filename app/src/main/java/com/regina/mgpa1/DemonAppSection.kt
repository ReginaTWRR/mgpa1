package com.regina.mgpa1

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.disableHotReloadMode
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

//==CLASSES

//A class that implements the Demon section of the App
class DemonAppSection : AppSection("Demon List") {
    @Composable
    override fun SectionLayout() {
        val scope = rememberCoroutineScope()
        val LevelsList = produceState(initialValue = emptyList<Level>()) {value = GetLevelList() }


        LazyColumn(modifier = Modifier.fillMaxSize()) {
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
