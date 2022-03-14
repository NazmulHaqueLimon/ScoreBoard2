package com.example.scoreboard.data.repositories

import com.example.scoreboard.data.dao.MatchDao
import com.example.scoreboard.data.dao.ScoreDao
import com.example.scoreboard.data.dao.TeamDao
import com.example.scoreboard.data.dao.TeamPlayersDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchInfoRepository @Inject constructor(
    private val matchDao: MatchDao,

)  {

    fun getTeamMatchAndScore() = matchDao.getMatchTeamAndScore()

    fun getMatchPlayerScorePlayer(matchId: String) = matchDao.getMatchPlayerScorePlayers(matchId)
    fun getMatchTeamScoreTeam(matchId: String)= matchDao.getMatchTeamScoreTeams(matchId)



}