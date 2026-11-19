package com.regina.mgpa1.ErrorUIs

import androidx.compose.runtime.Composable

//==INTERFACES

interface ErrorUIInterface{

    @Composable
    fun ErrorLayout(){
        println("hi")
    }
}

abstract class ErrorUIBase: ErrorUIInterface{

}