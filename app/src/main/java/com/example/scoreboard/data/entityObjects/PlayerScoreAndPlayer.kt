package com.example.scoreboard.data.entityObjects

import androidx.room.Embedded
import androidx.room.Relation

data class PlayerScoreAndPlayer(
    @Embedded val score:PlayersScore,
    @Relation(
        parentColumn = "playerId",
        entityColumn = "playerId",
    )
    val player :Player
)
