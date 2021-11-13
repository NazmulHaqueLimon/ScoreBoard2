package com.example.scoreboard.data.objects

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "matches")
data class Match(
    @PrimaryKey
    val matchId:String = UUID.randomUUID().toString(),
    val teamA_id: String,
    val teamB_id:String,
    var format: Int =0,
    var ground: String,
    var toss : String

    )
