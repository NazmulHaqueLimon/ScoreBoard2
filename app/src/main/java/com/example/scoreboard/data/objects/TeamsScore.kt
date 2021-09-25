package com.example.scoreboard.data.objects

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teamScore")
data class TeamsScore(
    @PrimaryKey
    val teamId :String,
    val matchId : String,

    val totalRun : Int=0,
    val wickLost : Int=0,
    val sr : Double = 0.0,
    val extra :Int =0,


    val overPlayed: Int=0,
    val wicketTaken :Int=0

)

