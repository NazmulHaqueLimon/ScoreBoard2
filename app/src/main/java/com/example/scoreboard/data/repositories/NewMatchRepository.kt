package com.example.scoreboard.data.repositories

import androidx.lifecycle.LiveData
import com.example.scoreboard.data.dao.MatchDao
import com.example.scoreboard.data.dao.PlayerDao
import com.example.scoreboard.data.dao.TeamDao
import com.example.scoreboard.data.dao.TeamPlayersDao
import com.example.scoreboard.data.objects.Match
import com.example.scoreboard.data.objects.Player
import com.example.scoreboard.data.objects.Team
import com.example.scoreboard.data.objects.TeamPlayers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewMatchRepository  @Inject constructor(
    private val playerDao: PlayerDao,
    private val matchDao: MatchDao,
    private val teamDao: TeamDao,
    private val teamPlayerDao : TeamPlayersDao
){

    suspend fun saveMatch(match : Match)=matchDao.insertMatch(match)

    suspend fun savePlayers(players: List<Player>) =playerDao.insertPlayers(players)

    suspend fun saveTeam(team: Team) =teamDao.insertTeam(team)

    suspend fun saveTeamPlayers(teamPlayer: TeamPlayers) {
        teamPlayerDao.insertTeamAndPlayer(teamPlayer)
    }

}