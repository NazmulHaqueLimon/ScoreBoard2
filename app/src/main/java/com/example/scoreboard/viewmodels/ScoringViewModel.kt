package com.example.scoreboard.viewmodels


import androidx.lifecycle.*
import com.example.scoreboard.data.MatchState
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

    val isScoringEnabled_ =MutableLiveData<Boolean>()
    val isScoringEnabled :LiveData<Boolean> =isScoringEnabled_

    val stateList = mutableListOf<MatchState>()
//    val lastState = stateList.last()

    fun setMatchId(id:String){
        matchId.value =id
    }

    val scoringTitleText :String ="teamA won the toss and elected to bat First"
    /**
     * matchId...need to save in savedStateHandle*/
    private val matchId: MutableStateFlow<String> = MutableStateFlow(
        savedState[MATCH_ID] ?: NO_MATCH_ID
    )
    init {
        viewModelScope.launch {
            matchId.collect { mid->
                savedState[MATCH_ID] = mid
            }
        }
    }
    /**
     * get the match from database using matchId
     * */
    @ExperimentalCoroutinesApi
    val match :LiveData<Match> =matchId.flatMapLatest {
            repository.getMatch(it)

    }.asLiveData()
    
    /**
     * get the teams from the database
     * */
     @ExperimentalCoroutinesApi
      val teamA = match.switchMap { match ->
        repository.getTeamWithPlayers(match.teamA_id).asLiveData()
     }
    @ExperimentalCoroutinesApi
     val teamB = match.switchMap { match ->
        repository.getTeamWithPlayers(match.teamB_id).asLiveData()
    }

    /**
     * get the batting team with players
     */
    val _battingTeamWithPlayers = MutableLiveData<TeamWithPlayers>()
    val battingTeamWithPlayers:LiveData<TeamWithPlayers> =_battingTeamWithPlayers

    @ExperimentalCoroutinesApi
    val batsmans: LiveData<List<Player>> = Transformations.map(battingTeamWithPlayers) {
        it.playerList
    }


    /**
     * get the bowling team with players
     */
    @ExperimentalCoroutinesApi
    val _bowlingTeamWithPlayers  = MutableLiveData<TeamWithPlayers>()
    @ExperimentalCoroutinesApi
    val bowlingTeamWithPlayers :LiveData<TeamWithPlayers> = _bowlingTeamWithPlayers

    @ExperimentalCoroutinesApi
    val bowlers: LiveData<List<Player>> = Transformations.map(bowlingTeamWithPlayers) {
        it.playerList

    }


    /**
     * selected batsman from the dropdowns
     * */
    val _isFirstInningsDone = MutableLiveData<Boolean>(false)
    val isFirstInningsDone:LiveData<Boolean> =_isFirstInningsDone

    private val _isScoreSheetCreated = MutableLiveData<Boolean>(false)
    private val isScoreSheetCreated:LiveData<Boolean> =_isScoreSheetCreated

    private val _batsmanAOnStrike = MutableLiveData<Boolean>(true)
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
        player.playerId.let {
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
        player.playerId.let {
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
        player.playerId.let {
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


    private fun updateBatsmanScore(playerScore:PlayersScore, state: MatchState){
        val newScore :PlayersScore
        when (state.run_bat) {
            1 -> {
                newScore = if (state.nb){
                    playerScore.copy(totalRun = playerScore.totalRun+state.run_bat)
                }else{
                    playerScore.copy(totalRun = playerScore.totalRun+state.run_bat, ballFaced = playerScore.ballFaced+1)
                }
                changeStrike()
            }
            2 -> {
                newScore = if (state.nb){
                    playerScore.copy(totalRun = playerScore.totalRun+state.run_bat)
                }else{
                    playerScore.copy(totalRun = playerScore.totalRun+state.run_bat, ballFaced = playerScore.ballFaced+1)
                }

            }
            3 -> {
                newScore = if (state.nb){
                    playerScore.copy(totalRun = playerScore.totalRun+state.run_bat)
                }else{
                    playerScore.copy(totalRun = playerScore.totalRun+state.run_bat, ballFaced = playerScore.ballFaced+1)
                }
                changeStrike()
            }
            4 -> {
                newScore = if (state.nb){
                    playerScore.copy(totalRun = playerScore.totalRun+state.run_bat, fours = playerScore.fours+1)
                }else{
                    playerScore.copy(totalRun = playerScore.totalRun+state.run_bat, fours = playerScore.fours+1, ballFaced = playerScore.ballFaced+1)
                }
            }
            6 -> {
                newScore = if (state.nb){
                    playerScore.copy(totalRun = playerScore.totalRun+state.run_bat, sixes = playerScore.sixes+1)
                }else{
                    playerScore.copy(totalRun = playerScore.totalRun+state.run_bat, sixes = playerScore.sixes+1, ballFaced = playerScore.ballFaced+1)
                }
            }
            else ->{
                newScore = if (state.nb){
                    playerScore.copy(totalRun = playerScore.totalRun+state.run_bat)
                }else{
                    playerScore.copy(totalRun = playerScore.totalRun+state.run_bat, ballFaced = playerScore.ballFaced+1)
                }
            }
        }
        updatePlayerScore(newScore)
    }

    fun onBatsmanScore (run:Int){
        val state = MatchState(run_bat = run, ballCount = true)
        stateList.add(state)
        updateMatchState(state)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun updateMatchState(state: MatchState) {
        if (state.ballCount){
            //update player score
            updateBatsmanScore( getStrikerScore(),state)
            bowlersScore.value?.let {
                val newScore =it.copy(runGiven = it.runGiven+state.run_bat, overBowled = it.overBowled+1)
                updatePlayerScore(newScore)
            }
            battingTeamScore.value?.let {
                val newScore =it.copy(totalRun = it.totalRun+state.run_bat, ballPlayed = it.ballPlayed +1)
                updateTeamScore(newScore)
            }
        }

        if (state.nb){
            if (state.lb){
                bowlersScore.value?.let {
                    val newScore =it.copy(runGiven = it.runGiven+state.extra)
                    updatePlayerScore(newScore)
                }
                battingTeamScore.value?.let {
                    val newScore =it.copy(totalRun = it.totalRun+state.extra)
                    updateTeamScore(newScore)
                }
            }
            else if (state.bye){
                battingTeamScore.value?.let {
                    val newScore =it.copy(totalRun = it.totalRun+state.run_bat+1)
                    updateTeamScore(newScore)
                }
            }
            else{
                //update player score
                updateBatsmanScore( getStrikerScore(),state)
                //update bowler score
                bowlersScore.value?.let {
                    val newScore =it.copy(runGiven = it.runGiven+state.run_bat)
                    updatePlayerScore(newScore)
                }
                //update batting team score
                battingTeamScore.value?.let {
                    val newScore =it.copy(totalRun = it.totalRun+state.run_bat)
                    updateTeamScore(newScore)
                }
            }
        }
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getStrikerScore(): PlayersScore {
        return when(batsmanAOnStrike.value){
            true -> batsmanAScore.value!!
            else -> batsmanBScore.value!!
        }
    }
//    @ExperimentalCoroutinesApi
//    fun updateBatsmanScore(runsTaken:Int){
//        when(batsmanAOnStrike.value){
//            true ->{
//                batsmanAScore.value?.let {
//                    updatePlayerScore(it,runsTaken) }
//            }
//            else -> {
//                batsmanBScore.value?.let {
//                    updatePlayerScore(it,runsTaken) }
//            }
//        }
//
//    }


    @ExperimentalCoroutinesApi
    fun updateScore(runsTaken:Int){

        when(batsmanAOnStrike.value){
            true ->{
               // batsmanAScore.value?.let { updatePlayerScore(it,runsTaken) }
            }
            else -> {
               // batsmanBScore.value?.let { updatePlayerScore(it,runsTaken) }
            }
        }

        battingTeamScore.value?.let {
            val newScore =it.copy(totalRun = it.totalRun+runsTaken, ballPlayed = it.ballPlayed +1)
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
    private fun updateTeam(team: Team){
        viewModelScope.launch {
            repository.updateTeam(team)
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
    @ExperimentalCoroutinesApi
    fun inningsBreak(){

        battingTeamWithPlayers.value?.team?.let {
            val team =it.copy(isBat = false)
            updateTeam(team)
        }
        bowlingTeamWithPlayers.value?.team?.let {
            val team =it.copy(isBat = true)
            updateTeam(team)
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

    @ExperimentalCoroutinesApi
    private fun updateBattingTeamExtra(score: Int,extra:Int) {
        battingTeamScore.value?.let {
            val newScore =it.copy(totalRun = it.totalRun+score+extra, extra = it.extra+extra)
            updateTeamScore(newScore)
        }
    }
    @ExperimentalCoroutinesApi
    fun updateExtra(score: Int, type: String) {
        when(type){
            "nbBAT" ->{
                //teamScore +extra 1
                updateBattingTeamExtra(score,0)
                //strikerScore +score

                //bowlerScore +extra+score
            }
            "nbB YE" ->{
                //bowlerScore +extra1
                //battingTeamScore +extra1+score
                updateBattingTeamExtra(score,1)

            }
            "nbLB" ->{
                //bowlerScore +extra +score
                //battingTeam +extra+score
                updateBattingTeamExtra(score,1)
                //striker +score
            }
            "LB" ->{
                //bowlerScore +extra +score
                //battingTeam +extra+score
                updateBattingTeamExtra(score,0)
                //striker +score
            }

            "wideBYE" ->{
                //battingTeam +byeRun+extra
                updateBattingTeamExtra(score,1)
                //bowlerScore +extra1

            }
            "BYE" ->{
                //battingTeam +score
                updateBattingTeamExtra(score,0)
            }

        }

    }


    companion object {
        private const val NO_MATCH_ID = "NO_MATCH_ID"
        private const val MATCH_ID = "MATCH_ID"
    }


}
