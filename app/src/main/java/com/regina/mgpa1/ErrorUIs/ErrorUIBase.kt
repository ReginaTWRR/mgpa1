package com.regina.mgpa1.ErrorUIs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.Bullet
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.regina.mgpa1.viewModels.DemonListViewModel

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

//==FUNCTIONS

// A function to render the error message involving internal server errors
@Composable
fun ServerErrorUI(contactMessage: String, isClientLiable: Boolean = true) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Hello! The server was unable to complete your request. Please try again later.", textAlign = TextAlign.Center)

        if (isClientLiable) {
            Spacer(modifier = Modifier.height(10.dp))
            Text("In the meantime, please check your:", textAlign = TextAlign.Center)
            Text(buildAnnotatedString {
                withBulletList(bullet = Bullet.Default) {
                    withBulletListItem { append("Network settings") }
                    withBulletListItem { append("Firewall setup") }
                    withBulletListItem { append("Proxy settings") }
                    withBulletListItem { append("DNS configuration") }
                }
            })
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text("If this problem persists, $contactMessage", textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(10.dp))
        RefreshButton()
    }
}

// A function to define and render the standard refresh button when an error is encountered
@Composable
fun RefreshButton() {
    val listViewModel = viewModel<DemonListViewModel>()

    Button(
        onClick = {
            listViewModel.refreshLevelList()
        }
    ) {
        Text("Refresh")
    }
}
