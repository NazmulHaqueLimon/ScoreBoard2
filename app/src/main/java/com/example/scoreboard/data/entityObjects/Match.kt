package com.example.scoreboard.data.entityObjects

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "matches")
data class Match(
    @PrimaryKey
    val matchId:String = UUID.randomUUID().toString(),
    val teamA_id: String,
    val teamB_id:String,
    val format: Int =0,
    val isFinished :Boolean =false,
    val ground: String,
    val toss : String
    )
