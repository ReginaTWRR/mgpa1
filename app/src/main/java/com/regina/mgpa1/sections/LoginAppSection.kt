package com.regina.mgpa1.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

//==CLASSES

//A class that implements the Login section of the App
class LoginAppSection : AppSection(header = "Login") {
    //Basically, mutableStateOf creates a observable value that calls a recompose when it is updated
    var loggedIn: Boolean by mutableStateOf(false)

    @Composable
    override fun SectionLayout() {
        if(loggedIn){
            UserPage()
        }
        else{
            loginPage()
        }
    }

    @Preview
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
                        if(email.isNotEmpty() && password.isNotEmpty()){
                            loggedIn = true
                        }
                    }
                ){
                    Text(text = "Login")
                }
            }
        }
    }

    @Composable
    fun UserPage(){
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
