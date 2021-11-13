package com.example.scoreboard.viewmodels


import androidx.lifecycle.*
import com.example.scoreboard.data.objects.*
import com.example.scoreboard.data.repositories.ScoringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class ScoringViewModel @Inject internal constructor(

    private val repository: ScoringRepository,
    private val savedState: SavedStateHandle

): ViewModel(){
    val scoringTitleText :String ="teamA won the toss and elected to bat First"
    /**
     * matchId...need to save in savedStateHandle*/
    private val matchId: MutableStateFlow<String> = MutableStateFlow(
        savedState.get(MATCH_ID) ?: NO_MATCH_ID
    )
    init {
        viewModelScope.launch {
            matchId.collect { mid->
                savedState.set(MATCH_ID, mid )
            }
        }
    }
    /**
     * get the match from database using matchId
     * */
    @ExperimentalCoroutinesApi
    val match :LiveData<Match> =matchId.flatMapLatest {
        if (it == MATCH_ID){
            repository.getMatch(it)
        }else{
            repository.getMatch(it)
        }
    }.asLiveData()


    /**
     * get the teams from the database
     * */
     @ExperimentalCoroutinesApi
     private val teamA = match.switchMap { match ->
        repository.getTeamWithPlayers(match.teamA_id).asLiveData()
    }
    @ExperimentalCoroutinesApi
    private val teamB = match.switchMap {match ->
        repository.getTeamWithPlayers(match.teamB_id).asLiveData()
    }


    @ExperimentalCoroutinesApi
    fun openScoreSheet(){
        val teamAScore = teamA.value?.team?.let {
            match.value?.let {
                    it1 -> TeamsScore(teamId = it.teamId ,matchId = it1.matchId)
            }
        }
        if (teamAScore != null) {
            createTeamScoreSheet(teamAScore)
        }
        val teamBScore = teamB.value?.team?.let {
            match.value?.let {
                    it1 -> TeamsScore(teamId = it.teamId ,matchId = it1.matchId)
            }
        }
        if (teamBScore != null) {
            createTeamScoreSheet(teamBScore)
        }

        teamA.value?.playerList?.map {player ->
            val score = match.value?.let { PlayersScore(playerId = player.id , matchId = it.matchId) }
            if (score != null) {
                createPlayersScoreSheet(score)
            }
        }
        teamB.value?.playerList?.map {player ->
            val score = match.value?.let { PlayersScore(playerId = player.id , matchId = it.matchId) }
            if (score != null) {
                createPlayersScoreSheet(score)
            }
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




    /**
     * get the batting team with players
     */

    @ExperimentalCoroutinesApi
    val battingTeamWithPlayers :LiveData<TeamWithPlayers> = getBattingTeam()

    @ExperimentalCoroutinesApi
    private fun getBattingTeam():LiveData<TeamWithPlayers>{
        return if (teamA.value?.team?.batFirst == true){
            teamA
        }else{
            teamB
        }
    }
    /**
     * get the bowling team with players
     */
    @ExperimentalCoroutinesApi
    val bowlingTeamWithPlayers :LiveData<TeamWithPlayers> = getBowlingTeam()

    @ExperimentalCoroutinesApi
    private fun getBowlingTeam():LiveData<TeamWithPlayers>{
        return if (teamA.value?.team?.batFirst == true){
            teamB
        }else{
            teamA
        }
    }

    /**
     * selected batsman from the dropdowns
     * */
    val _onStrike = MutableLiveData<Player>()
    val onStrike:LiveData<Player> =_onStrike
    /**
     * nonStriker from batsman2 dropdowns
     */
    val _nonStrike = MutableLiveData<Player>()
    val nonStrike:LiveData<Player> =_nonStrike
    /**
     * bowler from bowlerList dropdowns
     */
    val _bowler =MutableLiveData<Player>()
    val bowler:LiveData<Player> =_bowler

    /**
     * Striker score
     */
    @ExperimentalCoroutinesApi
    val strikerScore :LiveData<PlayersScore> = onStrike.switchMap {player->
        player.id.let {
            match.switchMap { match->
                repository.getPlayerScore(it,match.matchId).asLiveData()
            }
        }
    }

    /**
     * nonStriker score
     */
    @ExperimentalCoroutinesApi
    val nonStrikerScore :LiveData<PlayersScore> = nonStrike.switchMap {player->
        player.id.let {
            match.switchMap { match->
                repository.getPlayerScore(it,match.matchId).asLiveData()
            }
        }
    }

    /**
     * bowlers score
     */
    @ExperimentalCoroutinesApi
    val bowlersScore :LiveData<PlayersScore> = bowler.switchMap {player->
        player.id.let {
            match.switchMap { match->
                repository.getPlayerScore(it,match.matchId).asLiveData()
            }
        }
    }
    /**
     * getting the batting team  score
     */
    @ExperimentalCoroutinesApi
    val battingTeamScore :LiveData<TeamsScore> = battingTeamWithPlayers.switchMap { it ->
        it.team.teamId.let { teamId->
            match.switchMap {
                repository.getTeamScore(teamId,it.matchId).asLiveData()
            }
        }
    }
    /**
     * getting the bowling team  score
     */
    @ExperimentalCoroutinesApi
    val bowlingTeamScore :LiveData<TeamsScore> = bowlingTeamWithPlayers.switchMap { it ->
        it.team.teamId.let { teamId->
            match.switchMap {
                repository.getTeamScore(teamId,it.matchId).asLiveData()
            }
        }
    }



    @ExperimentalCoroutinesApi
    fun updateScore(runsTaken:Int){

        strikerScore.value?.let {
            val playerId =it.playerId
            val matchId =it.matchId

            val fours =it.fours +1
            val sixes =it.sixes +1
            val run =it.run +runsTaken
            val ballFaced =it.ballFaced +1
            val runGiven  =it.runGiven
            val overBowled =it.wicketTaken
            val wicketTaken =it.overBowled

            //val newScore =it.copy(run = it.run+runsTaken) /
            val newScore =PlayersScore(playerId,matchId,fours,sixes,run,ballFaced,runGiven,overBowled,wicketTaken)
            updatePlayerScore(newScore)

        }


        battingTeamScore.value?.let {
            val newScore =it.copy(totalRun = it.totalRun+runsTaken)
            updateTeamScore(newScore)
        }


    }




    //update player score...by making new object every time
    private fun updatePlayerScore(newScore:PlayersScore) {
        viewModelScope.launch {
            repository.updatePlayerScore(newScore)
        }
    }

    private fun updateTeamScore(newScore:TeamsScore){
        viewModelScope.launch {
            repository.updateTeamScore(newScore)
        }
    }
    fun onOut(){

    }


    fun changeStrike(){
        val temp = onStrike.value
        _onStrike.value =nonStrike.value
        _nonStrike.value =temp!!
    }



    fun setMatchId(id:String){
        matchId.value =id
    }

    companion object {
        private const val NO_MATCH_ID = "NO_MATCH_ID"
        private const val MATCH_ID = "MATCH_ID"
    }


}
