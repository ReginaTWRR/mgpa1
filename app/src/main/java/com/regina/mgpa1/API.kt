package com.regina.mgpa1

import android.R
import android.app.Application
import android.os.Build
import androidx.compose.material3.rememberRangeSliderState
import androidx.compose.ui.graphics.drawscope.Stroke
import java.util.UUID
import kotlinx.coroutines.*
//import kotlinx.datetime.DateTimeUnit
import java.net.HttpURLConnection
import java.time.Instant
import java.net.URL

//==Default Variables
val aredlLink = "https://api.aredl.net/v2" //Link to the aredl api. Concat the function GET in to this and pass it in the func

//==ENUMS
//the enums that some data classes use
enum class STATUS{
    PENDING,
    MAINLIST,
    LEGACY,
    REMOVED
}

//==DATA CLASSES
//Refer to the ARDEL/other APIS API for more details on these classes. Ill leave a comment as to where these came from

//These are innefficient. find out if I can extract only one or specific details from the api.

//List all Levels
data class Level(
    val id : UUID,
    val name : String,
    val position: Int,
    val points: Int,
    val status : STATUS,
    val requires_raw_footage: Boolean,
    val level_id:Int,
    val two_player: Boolean,
    val tags : List<String>,
    val description: String,
    val song: Int,
    val edel_enjoyment : Number,
    val is_edel_pending : Boolean,
    val gddl_tier: Number,
    val nlw_tier: String,
    val completed_by_user: Boolean
)

//Aredl Get Level Details
data class User(
    val id:UUID,
    val username:String,
    val global_name:String
)

//Aredl Get Level Details
//Note: Using Instant for the time, may be a bit strange tbh SO, if it breaks later on, use the kotlinx datetime
data class Verification(
    val id:UUID,
    val submitted_by: User,
    val mobile : Boolean,
    val video_url: String,
    val hide_video: Boolean,
    val acheived_at: Instant,
    val created_at: Instant,
    val updated_at: Instant

)

//https://img.youtube.com/vi/gB8slBPlOjc/maxresdefault.jpg
//https://img.youtube.com/vi/<VIDEO_ID>/hqdefault.jpg

//Aredl Get Level Details
data class LevelDetails(
    val id : UUID,
    val name : String,
    val position: Int,
    val points: Int,
    val status : STATUS,
    val requires_raw_footage: Boolean,
    val level_id:Int,
    val two_player: Boolean,
    val tags : List<String>,
    val description: String,
    val song: Int,
    val edel_enjoyment : Number,
    val is_edel_pending : Boolean,
    val gddl_tier: Number,
    val nlw_tier: String,
    val publisher: User,
    val verifications:List<Verification>
)

//==GETTER FUNCS
suspend fun TestGet(){
    val url = URL("$aredlLink/api/aredl/levels")
    println("Start")
    with(url.openConnection() as HttpURLConnection){
        requestMethod = "GET"
        println("Hi")
        inputStream.bufferedReader().use {
            it.lines().forEach { line ->
                println(line)
            }
        }
    }
//    println("hide123" + URL("https://google.com").readText())
    println("End")
}

//==HELPER FUNCS
//This function gets the thumbnail image off of youtube videos. please pass in the link properly.
//this needs the yt vid id which usually comes after the v= in yt link
fun BuildYTWebImage(YTLink : String,useMaxRes :Boolean = false) : String{
    val prefix = "https://img.youtube.com/vi/"
    val suffixHq = "/hqdefault.jpg"
    val suffixMaxRes = "/maxresdefault.jpg"
    val splitLink = YTLink.split("?v=")
    if(splitLink.size !in 2..2){
        //You should be using links like https://www.youtube.com/watch?v=JqZrKtzZk7U where theres only one v=
        //Check if the split link is a bad size. if it is, then we return this.
        println("Attempted to load invalid link at BUILDYTIWEBIMAGE $YTLink")
        //Return err image
        return "https://img.youtube.com/vi/%3CVIDEO_ID%3E/hqdefault.jpg"
    }
    //To do: for safety: make this valid the yt link.
    return if(useMaxRes){
        "$prefix${splitLink[1]}$suffixMaxRes"
    }
    else{
        "$prefix${splitLink[1]}$suffixHq"
    }
}

