package com.regina.mgpa1

import android.util.JsonReader
import androidx.annotation.RestrictTo
import java.util.UUID
import kotlinx.coroutines.*
import java.io.IOException
import java.net.HttpURLConnection
import java.time.Instant
import java.net.URL
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import kotlin.uuid.Uuid


//==Default Variables
val aredlEndPoint = "https://api.aredl.net/v2" //Link to the aredl api. Concat the function GET in to this and pass it in the func

//This is a data class meant for this file.
data class APIResponse(
    var responseCode:Int = -1, //the response code that the api returns
    var content: String = "Unsuccessful" //the content that the api returns
)

//==API FUNCS
//These funcs are for interacting with the API

//Ensure that the input is a proper aredl url func. Refer to the api docs or ask me(Eldrick) for more details
//btw, this only hosts a temporary connection to grab data once.
suspend fun getfromAPI(inputString : String): APIResponse{
    //Define URL
    val url = URL("$aredlEndPoint$inputString")
    var response : APIResponse = APIResponse()
    with(withContext(Dispatchers.IO) {url.openConnection()} as HttpURLConnection){
        try{
            requestMethod = "GET"
            response.responseCode = responseCode
            if(responseCode !in 200..300){
                response.content = responseMessage
            }
            else{
                //set response content to be the thing
               inputStream.bufferedReader().use{
                    val _response = StringBuffer()
                    var inputLine = it.readLine()
                    while(inputLine != null){
                        _response.append(inputLine)
                        inputLine = it.readLine()
                    }
                   response.content = _response.toString()
                }
            }
        }
        catch (e: IOException){
            println("Reading Error:${e.message}. Cause: ${e.cause}")
        }
        finally {
            disconnect()
        }
    }
    return response
}


//==JSON Parsers

suspend fun GetLevelList():List<Level>{
    val scope = CoroutineScope(Dispatchers.Default)
    val response = withContext(Dispatchers.IO){getfromAPI("/api/aredl/levels")}
    val results = Json.decodeFromString<List<Level>>(response.content)

    return results
}

suspend fun GetLevelDetails(LevelID : Int): LevelDetails{
//    val scope = CoroutineScope(Dispatchers.IO)
    val response = withContext(Dispatchers.IO){getfromAPI("/api/aredl/levels/$LevelID")}
    val results = Json.decodeFromString<LevelDetails>(response.content)
    return results
}

//==OTHER FUNCS
//These funcs are random, un categorised functions.

//This function gets the thumbnail image off of youtube videos. please pass in the link properly.
//this needs the yt vid id which usually comes after the v= in yt link
fun BuildYTWebImage(YTLink : String,useMaxRes :Boolean = false) : String{
    val prefix = "https://img.youtube.com/vi/"
    val suffixHq = "/hqdefault.jpg"
    val suffixMaxRes = "/maxresdefault.jpg"
    val splitLink = YTLink.split("=")
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

//So, this function is to grab a thumbnail from an established repo that the AREDL team made to house their thumbnails.
//They used an api (I think) but they rely on this repo to store and grab their images. I found that this was used in the higher/lower game
//so im following suite
fun GetLevelThumbnail(LevelID : Int) : String{
    return "https://raw.githubusercontent.com/All-Rated-Extreme-Demon-List/Thumbnails/main/levels/full/$LevelID.webp"
}

