package com.example.scoreboard.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.scoreboard.data.objects.Match
import com.example.scoreboard.data.objects.Player
import com.example.scoreboard.data.objects.Team
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {
   // @Query("SELECT * FROM teams ORDER BY name")
    //fun getPlayers(): Flow<List<Player>>

   // @Query("SELECT * FROM players WHERE playerId = :plantId")
   // fun getPlant(plantId: String): Flow<Player>

   // @Insert(onConflict = OnConflictStrategy.REPLACE)
    //suspend fun insertAll(plants: List<Team>)
   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertTeam(team: Team)
}