package com.example.scoreboard.data.objects

import androidx.room.Embedded
import androidx.room.Relation

data class MatchTeamScoreTeam(
    @Embedded val match: Match,

    @Relation(
        entity = TeamsScore ::class,
        parentColumn = "matchId",
        entityColumn = "matchId"
    )

    val teamScoreAndTeam: List<TeamScoreAndTeam>
)
