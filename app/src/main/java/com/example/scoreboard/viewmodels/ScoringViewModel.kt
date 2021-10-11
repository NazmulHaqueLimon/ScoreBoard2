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
    //val _matchId =MutableLiveData<String>()
    //private val matchId:LiveData<String> =_matchId
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
     * get the match from database using matchId*/
    @ExperimentalCoroutinesApi
    val match :LiveData<Match> =matchId.flatMapLatest {
        if (it == MATCH_ID){
            repository.getMatch(it)
        }else{
            repository.getMatch(it)
        }
    }.asLiveData()

    /**
     * get the teams from the database*/
     private val teamA = match.switchMap { match ->
        repository.getTeamWithPlayers(match.teamA_id).asLiveData()
    }
    private val teamB = match.switchMap {
        repository.getTeamWithPlayers(it.teamB_id).asLiveData()
    }


    /**
     * get the batting team with players
     */
   // private val _battingTeam = MutableLiveData<TeamWithPlayers>()
    //val battingTeam : LiveData<TeamWithPlayers> =_battingTeam


    val battingTeamWithPlayers :LiveData<TeamWithPlayers> = getBattingTeam()
    val bowlingTeamWithPlayers :LiveData<TeamWithPlayers> = getBowlingTeam()

    /**get the bowling team with players*/
    //private val _bowlingTeam  = MutableLiveData<TeamWithPlayers>()
    //val bowlingTeam:LiveData<TeamWithPlayers> =_bowlingTeam


    fun getBattingTeam():LiveData<TeamWithPlayers>{
        return if (teamA.value?.team?.batFirst == true){
            teamA
        }else{
            teamB
        }
    }
    fun getBowlingTeam():LiveData<TeamWithPlayers>{
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
    val strikerScore = onStrike.value?.id?.let {
        match.value?.let { it1 ->
            repository.getPlayerScore(it, it1.matchId)
        }
    }?.asLiveData()

    /**
     * nonStriker score
     */
    val nonStrikerScore = nonStrike.value?.id?.let {
        match.value?.let { it1 ->
            repository.getPlayerScore(it, it1.matchId)
        }
    }?.asLiveData()

    /**
     * bowlers score
     */
    val bowlersScore =bowler.value?.id?.let {
        match.value?.let { it1 ->
            repository.getPlayerScore(it, it1.matchId)
        }
    }?.asLiveData()




    fun changeStrike(){
        _onStrike.value =nonStrike.value
        _nonStrike.value =onStrike.value
    }


    fun updatePlayerScore(run:Int){
        val new = strikerScore?.value?.let {
            when(run){
                1 ->{
                    it.run ++
                    changeStrike()
                }
                2 ->
                    it.run +=2
                3 ->{
                    it.run +=3
                    changeStrike()
                }

                4 ->{
                    it.run +=4
                    it.fours ++
                }
                6 ->{
                    it.run +=6
                    it.sixes ++
                }

                else ->
                    it.run +=5
            }
        }
    }
    fun updateTeamScore(){

    }


    fun setMatchId(id:String){
        matchId.value =id
    }

    companion object {
        private const val NO_MATCH_ID = "NO_MATCH_ID"
        private const val MATCH_ID = "MATCH_ID"
    }


}