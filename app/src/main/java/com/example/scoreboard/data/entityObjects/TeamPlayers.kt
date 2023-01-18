package com.example.scoreboard.data.entityObjects

import androidx.room.Entity

@Entity(primaryKeys = ["teamId","playerId"])
data class TeamPlayers(
    val teamId: String,
    val playerId: String
    )