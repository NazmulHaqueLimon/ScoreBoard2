package com.example.scoreboard.data.objects

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playerScore")
data class PlayersScore(
    @PrimaryKey
    val playerId :String,
    val matchId : String,
    var fours :Int=0,
    var sixes :Int =0,
    var run : Int=0,
    val ballFaced : Int=0,

    val runGiven :Int =0,
    val overBowled: Int=0,
    val wicketTaken :Int=0

)
