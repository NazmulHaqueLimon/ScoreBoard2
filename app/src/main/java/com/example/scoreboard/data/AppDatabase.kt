package com.example.scoreboard.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.scoreboard.data.dao.*
import com.example.scoreboard.data.objects.TeamPlayers
import com.example.scoreboard.data.objects.*

import com.example.scoreboard.utils.DATABASE_NAME
/**
 * The Room Database that contains the player table, single match data table.
 *
 * Note that exportSchema should be true in production databases.
 */

@Database(entities = [Player::class, Team::class, TeamPlayers::class, Match::class, PlayersScore::class, TeamsScore::class],
           version = 6, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun playerDao(): PlayerDao
    abstract fun teamDao(): TeamDao
    abstract fun matchDao(): MatchDao
    abstract fun scoreDao(): ScoreDao
    abstract fun teamPlayersDao():TeamPlayersDao

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }
        // Create and pre-populate the database.
        private fun buildDatabase(context: Context): AppDatabase {

            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)

                .fallbackToDestructiveMigration()
                .build()
        }
    }
}