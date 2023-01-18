package com.example.scoreboard.data.dao

import androidx.room.*

import com.example.scoreboard.data.entityObjects.Team
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {

   @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertTeam(team: Team)

   @Update
   suspend fun updateTeam(team: Team)

   @Query("SELECT * FROM teams WHERE teamId = :team_id")
   fun getTeam(team_id:String):Flow<Team>
}