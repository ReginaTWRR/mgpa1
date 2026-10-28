//This file is for storing the JSON data to object classes

package com.regina.mgpa1

import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


//==ENUMS
//the enums that some data classes use
enum class STATUS{
    Pending,
    MainList,
    Legacy,
    Removed
}

//==Serializer Classes

//==DATA CLASSES
//Refer to the ARDEL/other APIS API for more details on these classes. I'll leave a comment as to where these came from
//List all Levels
@Serializable
data class Level(
    val id : String,
    val name : String,

    val position: Int? = null,
    val publisher_id : String,
    val points: Int,
    val status : STATUS,

    val requires_raw_footage: Boolean,
    val level_id:Long,
    val two_player: Boolean,
    val tags : List<String?>,
    val description: String ?= null,
    val song: Long ?= null,

    val edel_enjoyment : Float ?= null,
    val is_edel_pending : Boolean,
    val gddl_tier: Float ?= null,
    val nlw_tier: String ?= null,
    val completed_by_user: Boolean ?= null
)
//Aredl Get Level Details
@Serializable
data class User(
    val id:String,
    val username:String,
    val global_name:String
)

//Aredl Get Level Details
//Note: Using Instant for the time, may be a bit strange tbh SO, if it breaks later on, use the kotlinx datetime
@Serializable
data class Verification(
    val id:String,
    val submitted_by: User,
    val mobile : Boolean,
    val video_url: String,
    val hide_video: Boolean,
    val achieved_at: Instant,
    val created_at: Instant,
    val updated_at: Instant
)

//https://img.youtube.com/vi/gB8slBPlOjc/maxresdefault.jpg
//https://img.youtube.com/vi/<VIDEO_ID>/hqdefault.jpg

//Aredl Get Level Details
@Serializable
data class LevelDetails(
    val id : String,
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
    val edel_enjoyment : Float,
    val is_edel_pending : Boolean,
    val gddl_tier: Float? = null,
    val nlw_tier: String,
    val publisher: User,
    val verifications:List<Verification>
)
