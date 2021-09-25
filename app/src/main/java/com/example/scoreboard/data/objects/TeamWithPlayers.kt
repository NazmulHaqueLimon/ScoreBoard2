package com.example.scoreboard.data.objects

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TeamWithPlayers(
    @Embedded val team: Team,
    @Relation(
        parentColumn = "teamId",
        entityColumn = "id",
        associateBy = Junction(TeamPlayers::class)
    )
    val playerList: List<Player>
)