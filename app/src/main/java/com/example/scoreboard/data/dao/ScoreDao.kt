package com.example.scoreboard.data.dao

import androidx.room.*
import com.example.scoreboard.data.objects.MatchTeamTeamScore
import com.example.scoreboard.data.objects.Player
import com.example.scoreboard.data.objects.PlayersScore
import com.example.scoreboard.data.objects.TeamsScore
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayerScore(score: PlayersScore)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeamScore(score: TeamsScore)


    @Update(entity = PlayersScore::class)
    suspend fun updatePlayerScore(score: PlayersScore)

    @Update(entity = TeamsScore::class)
    suspend fun updateTeamScore(score: TeamsScore)

    @Update(entity = Player::class)
    suspend fun updatePlayer(batsman: Player)

    @Query("SELECT * FROM playerScore WHERE  playerId=:id AND matchId=:mId")
    fun getPlayerScore(id:String,mId:String): Flow<PlayersScore>

    @Query("SELECT * FROM teamScore WHERE  teamId=:tid AND matchId=:mId")
    fun getTeamScore(tid:String,mId:String): Flow<TeamsScore>

    @Transaction
    @Query("SELECT * FROM matches")
    fun getMatchTeamAndScore():Flow<List<MatchTeamTeamScore>>

}