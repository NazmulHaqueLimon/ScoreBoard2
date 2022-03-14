package com.example.scoreboard.data.objects

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "playerScore")
data class PlayersScore(
    @PrimaryKey
    val scoreId:String = UUID.randomUUID().toString(),

    val playerId :String,
    val matchId : String,

    var fours :Int=0,
    var sixes :Int =0,
    var totalRun : Int=0,
    var ballFaced : Int=0,

    var runGiven :Int =0,
    var overBowled: Int=0,
    var wicketTaken :Int=0

)
