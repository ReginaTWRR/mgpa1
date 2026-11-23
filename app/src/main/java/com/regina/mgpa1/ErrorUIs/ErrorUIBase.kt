package com.regina.mgpa1.ErrorUIs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

//==INTERFACES

interface ErrorUIInterface{
    //This function is called after the code and response message is defined.
    @Composable
    fun ErrorResolution(){}
}
//Error 600 is my own response header. It basically means no internet.
abstract class ErrorUIBase: ErrorUIInterface{
    val contactMessage = "Please contact us! @CustomerSupport"

    //This class calls the UI and auto places the appropriate UI
    @Composable
    fun ErrorLayout( responseCode:Int?, responseMessage:String){
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = "Response Code: $responseCode", textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Response Message: $responseMessage", textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(12.dp))
            ErrorResolution()
        }
    }
}
