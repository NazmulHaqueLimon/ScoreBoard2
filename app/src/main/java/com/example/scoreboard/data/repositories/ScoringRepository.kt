package com.example.scoreboard.data.repositories

import com.example.scoreboard.data.dao.*
import com.example.scoreboard.data.objects.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScoringRepository @Inject constructor(
    private val matchDao: MatchDao,
    private val playerDao: PlayerDao,
    private val teamPlayersDao: TeamPlayersDao,
    private val scoreDao: ScoreDao
){

    fun getMatch(id:String) = matchDao.getMatch(id)

    fun getTeamWithPlayers(teamId: String) =teamPlayersDao.getTeamWithPlayers(teamId)

    fun getTeamScore(teamId: String,matchId:String) =scoreDao.getTeamScore(teamId,matchId)

    fun getPlayers() =playerDao.getPlayers()

    fun getPlayerScore(id: String, mId: String) =scoreDao.getPlayerScore(id,mId)


}