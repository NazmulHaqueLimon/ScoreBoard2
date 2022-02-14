package com.example.scoreboard.data.repositories

import com.example.scoreboard.data.dao.MatchDao
import com.example.scoreboard.data.dao.ScoreDao
import com.example.scoreboard.data.dao.TeamDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchInfoRepository @Inject constructor(
    private val matchDao: MatchDao,
    private val teamDao: TeamDao,
    private val scoreDao: ScoreDao
)  {
    fun getMatches()=matchDao.getMatches()

    fun getTeam(id:String) =teamDao.getTeam(id)


    fun getTeamScore(teamId: String, matchId:String) =scoreDao.getTeamScore(teamId,matchId)


    fun getTeamMatchAndScore() = scoreDao.getMatchTeamAndScore()


}