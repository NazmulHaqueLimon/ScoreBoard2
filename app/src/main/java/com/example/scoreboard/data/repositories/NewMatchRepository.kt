package com.example.scoreboard.data.repositories

import androidx.lifecycle.LiveData
import com.example.scoreboard.data.dao.*
import com.example.scoreboard.data.objects.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewMatchRepository  @Inject constructor(
    private val playerDao: PlayerDao,
    private val matchDao: MatchDao,
    private val teamDao: TeamDao,
    private val teamPlayerDao : TeamPlayersDao,
    private val scoreDao: ScoreDao
){

    suspend fun saveMatch(match : Match)=matchDao.insertMatch(match)

    suspend fun savePlayers(players: List<Player>) =playerDao.insertPlayers(players)

    suspend fun saveTeam(team: Team) =teamDao.insertTeam(team)

    suspend fun saveTeamPlayers(teamPlayer: TeamPlayers) {
        teamPlayerDao.insertTeamPlayer(teamPlayer)
    }

    suspend fun createTeamScore(score: TeamsScore) = scoreDao.insertTeamScore(score)
    suspend fun createPlayerScore(score: PlayersScore) = scoreDao.insertPlayerScore(score)

}