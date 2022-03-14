package com.example.scoreboard.data.dao

import androidx.room.*
import com.example.scoreboard.data.objects.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: Match)

    @Query("SELECT * FROM matches WHERE matchId =:id")
    fun getMatch(id: String):Flow<Match>

    @Query("SELECT * FROM matches")
    fun getMatches(): Flow<List<Match>>


    @Transaction
    @Query("SELECT * FROM matches")
    fun getMatchTeamAndScore():Flow<List<MatchTeamScoreTeam>>

    @Transaction
    @Query("SELECT * FROM matches WHERE  matchId=:id")
    fun getMatchPlayerScorePlayers(id:String): Flow<MatchPlayerScorePlayer>

    @Transaction
    @Query("SELECT * FROM matches WHERE  matchId=:id")
    fun getMatchTeamScoreTeams(id:String): Flow<MatchTeamScoreTeam>

}