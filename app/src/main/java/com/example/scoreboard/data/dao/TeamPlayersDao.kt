package com.example.scoreboard.data.dao

import androidx.room.*
import com.example.scoreboard.data.objects.TeamPlayers
import com.example.scoreboard.data.objects.TeamWithPlayers
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamPlayersDao {

    @Transaction
    @Query("SELECT * FROM teams WHERE  teamId=:id")
    fun getTeamWithPlayers(id:String): Flow<TeamWithPlayers>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeamAndPlayer(teamPlayer: TeamPlayers)
}