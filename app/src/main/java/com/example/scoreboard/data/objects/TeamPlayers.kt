package com.example.scoreboard.data.objects

import androidx.room.Entity

@Entity(primaryKeys = ["teamId","playerId"])
data class TeamPlayers(
    val teamId: String,
    val playerId: String
    )