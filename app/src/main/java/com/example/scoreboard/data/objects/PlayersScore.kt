package com.example.scoreboard.data.objects

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playerScore",primaryKeys = ["playerId","matchId"])
data class PlayersScore(

    val playerId :String,
    val matchId : String,

    var fours :Int=0,
    var sixes :Int =0,
    var run : Int=0,
    var ballFaced : Int=0,

    var runGiven :Int =0,
    var overBowled: Int=0,
    var wicketTaken :Int=0

)
