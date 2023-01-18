package com.example.scoreboard.data.entityObjects

import androidx.room.Embedded
import androidx.room.Relation

data class MatchPlayerScorePlayer(
    @Embedded val match: Match,

    @Relation(
        entity = PlayersScore ::class,
        parentColumn = "matchId",
        entityColumn = "matchId"
    )

    val playerScoreAndPlayer : List<PlayerScoreAndPlayer>
)
