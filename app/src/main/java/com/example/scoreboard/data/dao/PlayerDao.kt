package com.example.scoreboard.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.scoreboard.data.objects.Player
import kotlinx.coroutines.flow.Flow

/**
 * The Data Access Object for the player class.
 */
@Dao
interface PlayerDao {

    @Query("SELECT * FROM players ORDER BY name")
    fun getPlayers(): Flow<List<Player>>

   // @Query("SELECT * FROM plants WHERE growZoneNumber = :growZoneNumber ORDER BY name")
    //fun getPlantsWithGrowZoneNumber(growZoneNumber: Int): Flow<List<Plant>>

    @Query("SELECT * FROM players WHERE id = :playerId")
    fun getPlayer(playerId: String): Flow<Player>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayers(players:List<Player>)
}