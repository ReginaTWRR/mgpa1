package com.regina.mgpa1.ErrorUIs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp

//==INTERFACES

interface ErrorUIInterface{
    //This function is called after the code and response message is defined.
    @Composable
    fun ErrorResolution(){}
}

abstract class ErrorUIBase: ErrorUIInterface{
    //This class calls the UI and auto places the appropriate UI
    @Composable
    fun ErrorLayout( responseCode:Int?, responseMessage:String){
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = "Response Code: $responseCode")
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Response Message:$responseMessage")
            Spacer(modifier = Modifier.height(12.dp))
            ErrorResolution()
        }
    }
}