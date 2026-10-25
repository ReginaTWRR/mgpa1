package com.regina.mgpa1

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

//==CLASSES

//A class that implements the Login section of the App
class LoginAppSection : AppSection("Login") {
    @Composable
    override fun SectionLayout() {
        Column() {
            Text(text = header)
        }
    }
}
