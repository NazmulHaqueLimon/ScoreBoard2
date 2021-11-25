package com.example.scoreboard.data.objects

data class MatchInfo(
    val teamA_name: String,
    val teamB_name:String,
    var format: Int =0,
    var ground: String,
    var totalRun :String,
    var wicketLost :String,
    var overPlayed :String
)
