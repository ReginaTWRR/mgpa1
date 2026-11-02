package com.regina.mgpa1.games

import android.R
import android.graphics.Paint
import android.icu.text.MessagePattern
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.core.view.WindowInsetsAnimationCompat
import coil.compose.AsyncImage
import com.regina.mgpa1.AppSection
import com.regina.mgpa1.GetLevelThumbnail
import kotlinx.coroutines.withContext

// THIS CLASS IS NOT IN USE
class Higher_LowerGame : AppSection("Higher/Lower"){
    @Preview
    @Composable
    override fun SectionLayout() {
        //Known information
        var KnownName: String = "Grief"
        var KnownPos: Int = 1
        var KnownLvlID : Int = 149992717
        //Guessing information
        var GuessingName: String = "Maniacal Chains"
        var GuessingLvlID : Int = 149992717
        var GuessingPos: Int = 32
        //Base box
        Column(
            modifier = Modifier.fillMaxSize()
            ){
            //Modifier Values(I placed box mod here cus the weight var is only accesible in the column)
            val boxModifier : Modifier = Modifier.weight(0.5f).background(Color("#2E2E5A".toColorInt()))
                .border(width = 2.dp,color = Color("#252535".toColorInt())).fillMaxWidth()

            //Stat tracker region
            Box(Modifier.weight(0.08f).background(Color("#1f2154".toColorInt())).fillMaxWidth()){
                Text(text = "Streak:", fontSize = 30.sp)
            }
            //Known Region
            Box(
                modifier = boxModifier.clickable(){
                    //Place logic here
                },
                contentAlignment = Alignment.Center
            ){
                //Test for loading the background image
                AsyncImage(
                    model = GetLevelThumbnail(KnownLvlID),
                    contentDescription = "Thumbnail",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
                RenderText(KnownName,KnownPos)
            }
            //Unknown Region
            Box(
                modifier = boxModifier.clickable(){
                    //Place logic here
                },
                contentAlignment = Alignment.Center
            ){
                AsyncImage(
                    model = GetLevelThumbnail(GuessingLvlID),
                    contentDescription = "Thumbnail",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
                RenderText(GuessingName,-1)
            }
        }
    }

    //Bad fun name
    @Composable
    fun RenderText(LevelName: String, Position:Int){
        var positionValue = Position.toString()
        if(Position < 0)
            positionValue = "???"
        Text(text = "$LevelName #$positionValue", fontSize = 40.sp,color = Color.White, textAlign = TextAlign.Center)
    }
}