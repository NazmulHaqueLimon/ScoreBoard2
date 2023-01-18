package com.example.scoreboard.data.entityObjects

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "teamScore")
data class TeamsScore(
    @PrimaryKey
    val scoreId :String= UUID.randomUUID().toString(),

    val teamId :String,
    val matchId : String,

    var totalRun : Int=0,
    var wickLost : Int=0,

    var extra :Int =0,

    var ballPlayed: Int=0,
    var wicketTaken :Int=0

)

