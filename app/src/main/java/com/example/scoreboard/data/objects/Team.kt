package com.example.scoreboard.data.objects

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "teams")
data class Team(
    @PrimaryKey
    val teamId :String = UUID.randomUUID().toString(),
    val name: String?,
    var batFirst:Boolean=false,
    val totalMatch:Int=0,
    val totalWin: Int=0,
    val totalLoses: Int=0
)
