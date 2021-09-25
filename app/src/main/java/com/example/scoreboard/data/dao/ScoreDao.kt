package com.example.scoreboard.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.scoreboard.data.objects.Match
import com.example.scoreboard.data.objects.PlayersScore
import com.example.scoreboard.data.objects.TeamsScore
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {

    @Query("SELECT * FROM playerScore WHERE  playerId=:id AND matchId=:mId")
    fun getPlayerScore(id:String,mId:String): Flow<PlayersScore>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayerScore(score: PlayersScore)



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeamScore(score: TeamsScore)

    @Query("SELECT * FROM teamScore WHERE  teamId=:tid AND matchId=:mId")
    fun getTeamScore(tid:String,mId:String): Flow<TeamsScore>

}