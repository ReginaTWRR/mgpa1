package com.regina.mgpa1.logic

import androidx.compose.runtime.Composable
import com.regina.mgpa1.ErrorUIs.E400UI
import com.regina.mgpa1.ErrorUIs.E404UI
import com.regina.mgpa1.ErrorUIs.E408UI
import com.regina.mgpa1.ErrorUIs.E410UI
import com.regina.mgpa1.ErrorUIs.E511UI
import com.regina.mgpa1.ErrorUIs.E600UI
import com.regina.mgpa1.ErrorUIs.EDefaulUI
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

//==Default Variables
val aredlEndPoint = "https://api.aredl.net/v2" //Link to the aredl api. Concat the function GET in to this and pass it in the func
//val aredlEndPoint = "https://q6ckqq.mockapi.dog" //Link to the aredl api. Concat the function GET in to this and pass it in the func

//This is a data class meant for this file.
data class APIResponse(
    var responseCode:Int = -1, //the response code that the api returns
    var content: String = "Unsuccessful", //the content that the api returns
    var URL : String = ""
)

//==API FUNCS
//These funcs are for interacting with the API

//Ensure that the input is a proper aredl url func. Refer to the api docs or ask me(Eldrick) for more details
//btw, this only hosts a temporary connection to grab data once.
suspend fun getfromAPI(inputString : String): APIResponse{
    //Define URL
    val url = URL("$aredlEndPoint$inputString")
    val response : APIResponse = APIResponse()
    with(withContext(Dispatchers.IO) {url.openConnection()} as HttpURLConnection){
        try{
            requestMethod = "GET"
            response.responseCode = responseCode
            if(responseCode !in 200..300){
                response.content = responseMessage
                response.URL = url.toString()
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
                   response.URL = url.toString()
                }
            }
        }
        catch (e: IOException){
            when(e){
                is UnknownHostException -> {
                    response.responseCode = 600
                    response.content = "No internet connection or DNS lookup failed"
                }
                is SocketTimeoutException -> {
                    response.responseCode = 600
                    response.content = "Connection Time out."
                }
                is ConnectException -> {
                    response.responseCode = 600
                    response.content = "Connection Refusal."
                }
                else -> {
                    println("code${response.responseCode}")
                    println("Reading Error:${e.message}. Cause: ${e.cause}")
                }
            }

        }
        finally {
            disconnect()
        }
    }
    return response
}


//==JSON Parsers

suspend fun GetLevelList():List<Level>{
//   val scope = CoroutineScope(Dispatchers.Default)
    val response = withContext(Dispatchers.IO){getfromAPI("/api/aredl/levels")}
    var results:List<Level>

    if(response.responseCode in 200..300){
        results =  Json.decodeFromString<List<Level>>(response.content)
    }
    else{
        val errorLevel = Level("-1","Error",response.responseCode,response.content,0, STATUS.Removed,false,0,false,listOf(null),null,null,null,false,null,null,null)
        results = listOf(errorLevel)
    }

    return results
}

suspend fun GetLevelDetails(LevelID : Int): LevelDetails{
    val response = withContext(Dispatchers.IO){getfromAPI("/api/aredl/levels/$LevelID")}
    val results : LevelDetails;

    if(response.responseCode in 200..300){
        results = Json.decodeFromString<LevelDetails>(response.content)
    }
    else{
        //Position is response code
        //Name is response content
        results = LevelDetails("-1",response.content,response.responseCode,0, STATUS.Removed,false,0,false,listOf(null),null,null,null,false,null,null,
            User("Error","Error","Error"),listOf())
    }

    return results
}

// A function to get the creators of a level
suspend fun GetLevelCreators(LevelID: Int): List<Creator> {
    val response = withContext(Dispatchers.IO) {
        getfromAPI(inputString = "/api/aredl/levels/$LevelID/creators")
    }
    val results: List<Creator>

    if (response.responseCode in 200..300) {
        results = Json.decodeFromString<List<Creator>>(response.content)
    } else {
        results = listOf(
            Creator(id = "-1", username = "Error", global_name = "Error")
        )
    }

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
    //Check what kind of link this is
    var splitLink: List<String> = listOf()
    splitLink = YTLink.split("=","&","youtu.be/","live/","?si=","?is")
    println(splitLink)

    val listofIllegalChars = listOf<String>("!","&","@","#")
    //Verify Video ID
    for (chars in listofIllegalChars) {
        if(splitLink[1].contains(chars)){
            println("Attempted to load invalid link at BUILDYTIWEBIMAGE $YTLink")
            //Return err image
            return "https://img.youtube.com/vi/%3CVIDEO_ID%3E/hqdefault.jpg"
        }
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

//This function finds out which error code is what and calls the appropriate UI
@Composable
fun ErrorHandler(ErrorLevel:Level){
    val ErrorCode = ErrorLevel.position
    val ErrorMessage = ErrorLevel.publisher_id
    errorCase(ErrorCode,ErrorMessage)
}
//Overload to use details instead
@Composable
fun ErrorHandler(ErrorDetails: LevelDetails){
    errorCase(ErrorDetails.position,ErrorDetails.name)
}

@Composable
private fun errorCase(ErrorCode:Int?,ErrorMessage: String){
    //Switch case
    when(ErrorCode){
        400-> E400UI().ErrorLayout(ErrorCode,ErrorMessage)
        404-> E404UI().ErrorLayout(ErrorCode,ErrorMessage)
        408-> E408UI().ErrorLayout(ErrorCode,ErrorMessage)
        410-> E410UI().ErrorLayout(ErrorCode,ErrorMessage)
        423-> E423UI().ErrorLayout(ErrorCode, ErrorMessage)
        429-> E429UI().ErrorLayout(ErrorCode, ErrorMessage)
        500-> E500UI().ErrorLayout(ErrorCode, ErrorMessage)
        502-> E502UI().ErrorLayout(ErrorCode, ErrorMessage)
        504-> E504UI().ErrorLayout(ErrorCode, ErrorMessage)
        511-> E511UI().ErrorLayout(ErrorCode,ErrorMessage)
        600-> E600UI().ErrorLayout(ErrorCode,ErrorMessage)
        else -> EDefaulUI().ErrorLayout(ErrorCode,ErrorMessage)
    }
}