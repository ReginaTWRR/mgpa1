package com.regina.mgpa1

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.regina.mgpa1.ui.theme.Mgpa1Theme

//==CLASSES
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Not entirely sure why but this needs to be thing
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        enableEdgeToEdge()
        //The main
        setContent {
            Mgpa1Theme {
                Content()
            }
        }
    }
}




//==FUNCTIONS

//The original intention of this function was to get around a problem i had. But, after redoing the code a bit(due to another problem), the first problem is gone.
//So, Im keeping this function to reuse in the preview func and the main func
@Composable
fun Content(){
    NavigationBar()
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    Mgpa1Theme {
        Content()
    }
}
