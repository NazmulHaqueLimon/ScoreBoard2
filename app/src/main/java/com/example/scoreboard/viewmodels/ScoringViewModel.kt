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
import java.lang.StringBuilder

import javax.inject.Inject

@HiltViewModel
class ScoringViewModel @Inject internal constructor(

    private val repository: ScoringRepository,
    private val savedState: SavedStateHandle

): ViewModel(){
    fun setMatchId(id:String){
        matchId.value =id
    }

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
    fun openTeamScoreSheet(teamId: String) {
        _isScoreSheetCreated.value=false
        match.value?.let {
            val score =TeamsScore(teamId ,it.matchId)
            createTeamScoreSheet(score)
        }
    }

    @ExperimentalCoroutinesApi
    fun openPlayerScoreSheet(playerId: String) {
        match.value?.let {
            val score =PlayersScore(playerId ,it.matchId)
            createPlayersScoreSheet(score)
            _isScoreSheetCreated.value=true
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
    val _isFirstInningsDone = MutableLiveData<Boolean>(false)
    val isFirstInningsDone:LiveData<Boolean> =_isFirstInningsDone

    val _isScoreSheetCreated = MutableLiveData<Boolean>(false)
    val isScoreSheetCreated:LiveData<Boolean> =_isScoreSheetCreated

    val _batsmanAOnStrike = MutableLiveData<Boolean>(true)
    val batsmanAOnStrike:LiveData<Boolean> =_batsmanAOnStrike

    val _batsmanA = MutableLiveData<Player>()
    val batsmanA:LiveData<Player> =_batsmanA
    /**
     * nonStriker from batsman2 dropdowns
     */

    val _batsmanB = MutableLiveData<Player>()
    val batsmanB:LiveData<Player> =_batsmanB
    /**
     * bowler from bowlerList dropdowns
     */
    val _bowler =MutableLiveData<Player>()
    val bowler:LiveData<Player> =_bowler

    /**
     * Striker score
     */
    @ExperimentalCoroutinesApi
    val batsmanAScore :LiveData<PlayersScore> = batsmanA.switchMap {player->
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
    val batsmanBScore :LiveData<PlayersScore> = batsmanB.switchMap {player->
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


    private fun updatePlayerScore(playerScore:PlayersScore, runsTaken:Int){
        val newScore :PlayersScore
        when (runsTaken) {
            1 -> {
                newScore = playerScore.copy(run = playerScore.run+runsTaken, ballFaced = playerScore.ballFaced+1)
                changeStrike()
            }
            2 -> {
                newScore = playerScore.copy(run = playerScore.run+runsTaken, ballFaced = playerScore.ballFaced+1)
            }
            3 -> {
                newScore = playerScore.copy(run = playerScore.run+runsTaken, ballFaced = playerScore.ballFaced+1)
                changeStrike()
            }
            4 -> {
                newScore = playerScore.copy(run = playerScore.run+runsTaken, fours = playerScore.fours+1, ballFaced = playerScore.ballFaced+1)
            }
            else ->{
                newScore = playerScore.copy(run = playerScore.run+runsTaken, ballFaced = playerScore.ballFaced+1, sixes = playerScore.sixes+1)
            }
        }
        updatePlayerScore(newScore)
    }

    @ExperimentalCoroutinesApi
    fun updateScore(runsTaken:Int){

        when(batsmanAOnStrike.value){
            true ->{
                batsmanAScore.value?.let { updatePlayerScore(it,runsTaken) }
            }
            else -> {
                batsmanBScore.value?.let { updatePlayerScore(it,runsTaken) }
            }
        }

        battingTeamScore.value?.let {
            val newScore =it.copy(totalRun = it.totalRun+runsTaken, overPlayed = (it.overPlayed +1)%6)
            updateTeamScore(newScore)
        }
        bowlersScore.value?.let {
            val newScore =it.copy(runGiven = it.runGiven+runsTaken, overBowled = it.overBowled+1)
            updatePlayerScore(newScore)
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


    fun changeStrike(){
        if (batsmanAOnStrike.value == true){
            _batsmanAOnStrike.value = false
        }
        else if (batsmanAOnStrike.value ==false){
            _batsmanAOnStrike.value =true
        }
    }


    fun onStrikerOut() {
        when(batsmanAOnStrike.value){
            true ->{
                onBatsmanAOut()
            }
            else ->
                onBatsmanBOut()
        }
    }

    fun onBatsmanAOut() {
        val batsman = batsmanA.value?.copy(isOut = true)
        if (batsman != null) {
            updatePlayer(batsman)
        }
    }

    private fun updatePlayer(batsman: Player) {
        viewModelScope.launch {
            repository.updatePlayer(batsman)
        }
    }

    fun onBatsmanBOut() {
        val batsman = batsmanB.value?.copy(isOut = true)
        if (batsman != null) {
            updatePlayer(batsman)
        }
    }

    fun updateExtra(score: Int, type: String) {
        when(type){
            "nbBAT" ->{
                //teamScore +extra 1
                //strikerScore +score
                //bowlerScore +extra+score
            }
            "nbBYE" ->{
                //bowlerScore +extra1
                //battingTeamScore +extra1+score

            }
            "nbLB" ->{
                //bowlerScore +extra +score
                //battingTeam +extra+score
                //striker +score
            }
            "LB" ->{
                //bowlerScore +extra +score
                //battingTeam +extra+score
                //striker +score
            }

            "wideBYE" ->{

            }
            "BYE" ->{

            }

        }

    }


    companion object {
        private const val NO_MATCH_ID = "NO_MATCH_ID"
        private const val MATCH_ID = "MATCH_ID"
    }


}
