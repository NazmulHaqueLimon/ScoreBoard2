package com.example.scoreboard.data.objects

import androidx.room.Embedded
import androidx.room.Relation

data class PlayerScoreAndPlayer(
    @Embedded val score:PlayersScore,
    @Relation(
        parentColumn = "playerId",
        entityColumn = "id",
    )
    val player :Player
)
