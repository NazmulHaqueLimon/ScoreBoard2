package com.example.scoreboard.data.objects

import androidx.room.Embedded
import androidx.room.Relation

data class MatchTeamTeamScore(
    @Embedded val match: Match,

    @Relation(
        entity = TeamsScore ::class,
        parentColumn = "matchId",
        entityColumn = "matchId"
    )

    val teamAndTeamScore: List<TeamAndTeamScore>
)
