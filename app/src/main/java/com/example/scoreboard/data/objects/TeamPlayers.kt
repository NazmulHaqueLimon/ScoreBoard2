package com.example.scoreboard.data.objects

import androidx.room.Entity

@Entity(primaryKeys = ["teamId","id"])
data class TeamPlayers(
    val teamId: String,
    val id: String
    )