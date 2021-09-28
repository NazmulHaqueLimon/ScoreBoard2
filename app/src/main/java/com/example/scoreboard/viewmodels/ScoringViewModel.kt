package com.example.scoreboard.viewmodels


import android.util.Log
import androidx.lifecycle.*
import com.example.scoreboard.data.objects.*
import com.example.scoreboard.data.repositories.ScoringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoringViewModel @Inject internal constructor(

    private val repository: ScoringRepository

): ViewModel(){
    val scoringTitleText :String ="teamA won the toss and elected to bat First"

    /**
     * matchId...need to save in savedStateHandle*/
    val _matchId =MutableLiveData<String>()
    private val matchId:LiveData<String> =_matchId

    /**
     * get the match from database using matchId*/
    val match :LiveData<Match> =matchId.switchMap {
        repository.getMatch(it).asLiveData()

    }

    /**
     * get the teams from the database*/
     private val teamA = match.switchMap { match ->
        repository.getTeamWithPlayers(match.teamA_id).asLiveData()
    }
    private val teamB = match.switchMap {
        repository.getTeamWithPlayers(it.teamB_id).asLiveData()
    }

    /**
     * create player score for eatch player playing the match
     */
    init {
        viewModelScope.launch {
            teamA.value?.playerList?.forEach { player ->
                val playerScore = match.value?.let { PlayersScore(matchId = it.matchId,playerId = player.id) }
                //repository.createPlayerScore(playerScore)

            }
        }
    }

    /**
     * get the batting team with players
     */
    private val _battingTeam : MutableLiveData<TeamWithPlayers> = MutableLiveData(getBattingTeam())
    val battingTeam : LiveData<TeamWithPlayers> =_battingTeam


    fun collectBatsmanNames(): List<String>? {
        return teamA.value?.playerList?.map {
            it.name.toString()
        }
       // return battingTeam.value?.playerList?.map { it.name.toString() }
    }
    fun collectBowlerNames():List<String>?{
        return bowlingTeam.value?.playerList?.map { it.name.toString() }
    }
    /**
     * batting team score
     */
    val battingTeamScore = battingTeam.value?.team?.let {
        match.value?.let { it1 ->
            repository.getTeamScore(it.teamId, it1.matchId)
        }
    }?.asLiveData()

    /**get the bowling team with players*/
    private val _bowlingTeam  :MutableLiveData<TeamWithPlayers> = MutableLiveData(getBowlingTeam())
    val bowlingTeam:LiveData<TeamWithPlayers> =_bowlingTeam
    /**
     * bowling team score
     */
    val bowlingTeamScore = bowlingTeam.value?.team?.let {
        match.value?.let { it1 ->
            repository.getTeamScore(it.teamId, it1.matchId)
        }
    }?.asLiveData()


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

    private fun getBattingTeam(): TeamWithPlayers? {
        return when(teamA.value?.team?.batFirst){
            true ->
                teamA.value
            else ->
                teamB.value
        }
    }

    private fun getBowlingTeam(): TeamWithPlayers? {
        return when(teamA.value?.team?.batFirst){
            true ->
                teamB.value
            else ->
                teamA.value
        }

    }

}