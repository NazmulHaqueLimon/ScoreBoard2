package com.example.scoreboard.data

import com.example.scoreboard.data.objects.Player
import com.example.scoreboard.data.objects.PlayersScore

data class MatchState(
    val extra:Int =0,
    val run_bat : Int=0,
    val playerScore :PlayersScore?=null,
    val ballCount : Boolean =false,
    val player_out : Player?=null,
    val wicket_type : String?=null,
    val bat :Boolean =false,
    val nb : Boolean=false,
    val wide :Boolean =false,
    val bye :Boolean =false,
    val lb :Boolean =false,
)
