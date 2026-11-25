package com.regina.mgpa1.sections

import android.graphics.Paint
import androidx.annotation.ColorInt
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.regina.mgpa1.ErrorUIs.preLoadedUI
import com.regina.mgpa1.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

//==CLASSES

//A class that implements the Login section of the App
class LoginAppSection : AppSection(header = "Login") {
    //Basically, mutableStateOf creates a observable value that calls a recompose when it is updated
    var loggedIn: Boolean by mutableStateOf(false)
    var Displayemail:String = "";
    @Composable
    override fun SectionLayout() {
        if(loggedIn){
            UserPage()
        }
        else{
            loginPage()
        }
    }

    @Composable
    fun loginPage() {
        //Input Field Variables
        var email by rememberSaveable() { mutableStateOf("")}
        //this stores a logic block. So that .let apply it and retrieve results
        val Emailerror = when{
            email.isEmpty() -> "Enter email here"
            !email.contains("@") -> "An Email needs an @"
            !email.substringAfter("@").contains(".")->"Add a domain,such as .com"
            else -> null
        }

        var password by rememberSaveable() { mutableStateOf("")}
        val Passworderror = when{
            password.isEmpty() -> "Please enter password"
            else -> null
        }

        //Parent Column to align everything in the center
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            //Nested column to properly align the text fields and header UI
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .background(color = MaterialTheme.colorScheme.background),
            ) {
                //EMAIL UI
                Text(text = "Email:", color = MaterialTheme.colorScheme.onBackground)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = email, onValueChange = {email = it},
                    isError = Emailerror!=null,
                    supportingText = {Emailerror?.let{Text(it)}},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(modifier = Modifier.height(10.dp))

                //PASSWORD UI
                Text(text = "Password:",color = MaterialTheme.colorScheme.onBackground)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(value = password, onValueChange = {password = it}, isError = Passworderror!=null, supportingText = {Passworderror?.let{Text(it)}})

                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        // TODO: add refresh animation here
                        if(Emailerror == null && Passworderror == null){
                            Displayemail = email;
                            loggedIn = true
                        }
                    }
                ){
                    Text(text = "Login")
                }
            }
        }
    }

    @Preview
    @Composable
    fun UserPage(){
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(100.dp))
            Image(
                painter = painterResource(R.drawable.defaultprofile), contentDescription = "Default profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(CircleShape).border(2.dp,MaterialTheme.colorScheme.onBackground,CircleShape)
            )
            Spacer(Modifier.height(50.dp))
            Text(text = "Username",color = MaterialTheme.colorScheme.onBackground, fontSize = 30.sp)
            Text(text = "$Displayemail",color = MaterialTheme.colorScheme.onBackground, fontSize = 30.sp)
            Spacer(Modifier.height(50.dp))
            //Temp UI
            Button(
                onClick = {
                    loggedIn = false
                }
            ){
                Text(text = "SignOut")
            }
        }

    }
}
