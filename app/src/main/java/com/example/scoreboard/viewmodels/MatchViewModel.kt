package com.example.scoreboard.viewmodels

import androidx.lifecycle.*
import com.example.scoreboard.data.objects.*
import com.example.scoreboard.data.repositories.NewMatchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject internal constructor(

    private val savedStateHandle: SavedStateHandle,
    private val repository : NewMatchRepository

): ViewModel() {

    val _teamFlag = MutableLiveData<String>()
    val teamFlag :LiveData<String> = _teamFlag

    val _teamA = MutableLiveData<Team>()
    val teamA : LiveData<Team> =_teamA

    val _teamB = MutableLiveData<Team>()
    val teamB: LiveData<Team> =_teamB

    val _over = MutableLiveData<Int>()
    var over: LiveData<Int> =_over

    val _toss = MutableLiveData<String>()
    val toss: LiveData<String> =_toss

    val _ground = MutableLiveData<String>()
    val ground: LiveData<String> =_ground

    /**
     * team players
     * */
    private val _teamAplayers = MutableLiveData<List<Player>>()
    private val teamAplayers :LiveData<List<Player>> =_teamAplayers

    private val _teamBplayers = MutableLiveData<List<Player>>()
    private val teamBplayers :LiveData<List<Player>> =_teamBplayers

     /**
     * creating the match from the method and provide it as a liveData
      * */
    private val _match =MutableLiveData<Match>()
    val match:LiveData<Match> =_match

    fun createMatch() {
        val id = teamA.value?.teamId
        val id2 = teamB.value?.teamId
        val frmt = over.value
        val grnd = ground.value
        val toss =toss.value

        val match= Match(
                    teamA_id = id!!,
                    teamB_id = id2!!,
                    format = frmt!!,
                    ground = grnd.toString(),
                    toss = toss.toString()
      )
        _match.value=match
    }
    /**
     * handle newMatchFragments textInputs and save in live data
     */
    fun collectTeamAndPlayers(team: Team ,pList:List<Player>){

        teamFlag.value.let {
            when(it){
                "A" -> {
                    _teamA.value = team
                    _teamAplayers.value = pList
                }
                "B" ->{
                    _teamB.value = team
                    _teamBplayers.value = pList
                }
            }
        }

    }

     fun saveTeamPlayers(){
         val teamAId = teamA.value?.teamId
         val teamBId = teamB.value?.teamId
         teamAplayers.value?.map { player ->
             val teamPlayer= teamAId?.let { TeamPlayers(it,player.id) }
             if (teamPlayer != null) {
                 insertTeamPlayers(teamPlayer)
             }
         }
         teamBplayers.value?.map {player->
             val teamPlayer= teamBId?.let { TeamPlayers(it,player.id) }
             if (teamPlayer != null) {
                 insertTeamPlayers(teamPlayer)

             }
         }

    }

     fun saveTeams() {
         teamA.value?.let { it ->
             insertTeam(it)
         }
         teamB.value?.let { it ->
             insertTeam(it)
         }

    }

     fun savePlayers() {
         teamAplayers.value?.let { insertPlayers(it) }
         teamBplayers.value?.let { insertPlayers(it) }

    }
    fun saveMatch() {
        match.value?.let {
             insertMatch(it)
        }
    }


    private fun insertPlayers(list: List<Player>){
        viewModelScope.launch {
            repository.savePlayers(list)
        }
    }
    private fun insertTeam(team: Team){
        viewModelScope.launch {
            repository.saveTeam(team)
        }
    }
    private fun insertMatch(match: Match){
        viewModelScope.launch {
            repository.saveMatch(match)
        }
    }
    private fun insertTeamPlayers(teamPlayers: TeamPlayers){
        viewModelScope.launch {
            repository.saveTeamPlayers(teamPlayers)
        }
    }
    @ExperimentalCoroutinesApi
    fun createScoreSheet() {
        teamAplayers.value?.map {
            openPlayerScoreSheet(it.id)
        }
        teamBplayers.value?.map {
            openPlayerScoreSheet(it.id)
        }
        teamA.value?.let {
            openTeamScoreSheet(it.teamId)
        }
        teamB.value?.let {
            openTeamScoreSheet(it.teamId)
        }

    }

    @ExperimentalCoroutinesApi
    fun openTeamScoreSheet(teamId: String) {
        match.value?.let {
            val score = TeamsScore(teamId = teamId , matchId = it.matchId)
            createTeamScoreSheet(score)
        }
    }

    @ExperimentalCoroutinesApi
    fun openPlayerScoreSheet(playerId: String) {
        match.value?.let {
            val score =PlayersScore(playerId = playerId, matchId = it.matchId)
            createPlayersScoreSheet(score)
        }
    }
    private fun createTeamScoreSheet(score: TeamsScore){
        viewModelScope.launch {
            repository.createTeamScore(score)
        }
    }
    private fun createPlayersScoreSheet(score: PlayersScore){
        viewModelScope.launch {
            repository.createPlayerScore(score)
        }
    }


}