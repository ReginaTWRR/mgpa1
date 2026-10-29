package com.regina.mgpa1.games

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsAnimationCompat
import coil.compose.AsyncImage
import com.regina.mgpa1.AppSection
import com.regina.mgpa1.GetLevelThumbnail
import kotlinx.coroutines.withContext


class Higher_LowerGame : AppSection("Higher/Lower"){
    @Preview
    @Composable
    override fun SectionLayout() {
        //Base box
        Column(
            modifier = Modifier.fillMaxSize()
            ){
            //Stat tracker region
            Box(Modifier.weight(0.1f).background(Color.Blue).fillMaxWidth()){
                Text(text = "Streak:", fontSize = 30.sp)
            }
            //Known Region
            Box(
                Modifier.weight(0.5f).background(Color.Red).fillMaxWidth()
                    .clickable(){
                        //Place logic here
                    }
            ){
                //Test for loading the background image
                AsyncImage(
                    model = GetLevelThumbnail(149992717),
                    contentDescription = "Thumbnail",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                Text(text = "Grief\n#1", fontSize = 40.sp)
            }
            //Unknown Region
            Box(
                Modifier.weight(0.5f).background(Color.Green).fillMaxWidth()
                    .clickable(){
                        //Place logic here
                        println("bye")
                    }
            ){
                Text(text = "Name\n#???", fontSize = 40.sp)

            }
        }
    }
}