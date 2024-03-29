package com.example.scoreboard.data.entityObjects

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "teams")
data class Team(
    @PrimaryKey
    val teamId :String = UUID.randomUUID().toString(),

    val name: String,
    var isBat: Boolean=false,
    var totalMatch:Int=0,
    var totalWin: Int=0,
    var totalLoses: Int=0
)
