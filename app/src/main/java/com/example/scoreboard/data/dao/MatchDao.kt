package com.example.scoreboard.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.scoreboard.data.objects.Match
import com.example.scoreboard.data.objects.Player
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: Match)

    @Query("SELECT * FROM matches WHERE matchId =:id")
    fun getMatch(id: String):Flow<Match>
}