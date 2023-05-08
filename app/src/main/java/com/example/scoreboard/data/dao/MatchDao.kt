package com.example.scoreboard.data.dao

import androidx.room.*
import com.example.scoreboard.data.entityObjects.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: Match)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatchState(state: MatchState)

    @Query("SELECT * FROM matches WHERE matchId =:id")
    fun getMatch(id: String):Flow<Match>

    @Query("SELECT * FROM matchState WHERE matchId = :matchId")
    fun getMatchState(matchId :String):Flow<MatchState>

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