package com.example.scoreboard.di

import android.content.Context
import com.example.scoreboard.data.AppDatabase
import com.example.scoreboard.data.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun providePlayerDao(appDatabase: AppDatabase): PlayerDao {
        return appDatabase.playerDao()
    }
    @Provides
    fun provideTeamDao(appDatabase: AppDatabase): TeamDao {
        return appDatabase.teamDao()
    }

    @Provides
    fun provideMatchDao(appDatabase: AppDatabase): MatchDao {
        return appDatabase.matchDao()
    }

    @Provides
    fun provideTeamPlayersDao(appDatabase: AppDatabase): TeamPlayersDao {
        return appDatabase.teamPlayersDao()
    }
    @Provides
    fun provideScoreDao(appDatabase: AppDatabase): ScoreDao {
        return appDatabase.scoreDao()
    }
}
















