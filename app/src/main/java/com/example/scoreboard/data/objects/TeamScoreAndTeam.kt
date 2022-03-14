package com.example.scoreboard.data.objects

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TeamScoreAndTeam(
    @Embedded val score:TeamsScore,
    @Relation(
        parentColumn = "teamId",
        entityColumn = "teamId",
    )
    val team :Team
)
