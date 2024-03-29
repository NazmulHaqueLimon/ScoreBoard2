package com.example.scoreboard.data.dao

import androidx.room.*
import com.example.scoreboard.data.entityObjects.TeamPlayers
import com.example.scoreboard.data.entityObjects.TeamWithPlayers
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamPlayersDao {

    @Transaction
    @Query("SELECT * FROM teams WHERE  teamId=:id")
    fun getTeamWithPlayers(id:String): Flow<TeamWithPlayers>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeamPlayer(teamPlayer: TeamPlayers)
}