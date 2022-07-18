package com.example.scoreboard.viewmodels

import androidx.lifecycle.*
import com.example.scoreboard.data.objects.*
import com.example.scoreboard.data.repositories.MatchInfoRepository
import com.example.scoreboard.data.repositories.ScoringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchDetailsViewmodel @Inject internal constructor(

    private val repository: MatchInfoRepository,
    private val scoringRepo :ScoringRepository,
    private val savedState: SavedStateHandle

): ViewModel() {


    /**
     * matchId...need to save in savedStateHandle*/
    private val matchId: MutableStateFlow<String> = MutableStateFlow(
        savedState.get(MATCH_ID) ?:NO_MATCH_ID
    )

    fun setMatchId(id:String){
        matchId.value =id
    }
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
        scoringRepo.getMatch(it)

    }.asLiveData()

    /**
     * get the teams from the database
     * */
    @ExperimentalCoroutinesApi
    val teamAWithPlayers = match.switchMap { match ->
        scoringRepo.getTeamWithPlayers(match.teamA_id).asLiveData()
    }

    @ExperimentalCoroutinesApi
    val teamBWithPlayers = match.switchMap { match ->
        scoringRepo.getTeamWithPlayers(match.teamB_id).asLiveData()
    }

    val _battingTeamAndScore = MutableLiveData<TeamScoreAndTeam>()
    val battingTeamAndScore  :LiveData<TeamScoreAndTeam> =_battingTeamAndScore

    val _bowlingTeamAndScore = MutableLiveData<TeamScoreAndTeam>()
    val bowlingTeamAndScore :LiveData<TeamScoreAndTeam> =_bowlingTeamAndScore


    /**getting the batting and bowling players in order to display batting teams score by default*/
    val _battingTeamWithPlayers = MutableLiveData<TeamWithPlayers>()
    val battingTeamWithPlayers  :LiveData<TeamWithPlayers> =_battingTeamWithPlayers

    val _bowlingTeamWithPlayers= MutableLiveData<TeamWithPlayers>()
    val bowlingTeamWithPlayers :LiveData<TeamWithPlayers> =_bowlingTeamWithPlayers

    private val _batsmanAndScores =MutableLiveData<List<PlayerScoreAndPlayer>>()
    val batsmanAndScores:LiveData<List<PlayerScoreAndPlayer>> =_batsmanAndScores

    private val _bowlerAndScores =MutableLiveData<List<PlayerScoreAndPlayer>>()
    val bowlerAndScores:LiveData<List<PlayerScoreAndPlayer>> =_bowlerAndScores

    @OptIn(ExperimentalCoroutinesApi::class)
    val matchPlayerScoreAndPlayer= match.switchMap { match ->
        match.matchId.let {
            repository.getMatchPlayerScorePlayer(it)
        }.asLiveData()
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    val matchTeamScoreTeam =match.switchMap { it ->
        it.matchId.let {
            repository.getMatchTeamScoreTeam(it)
        }.asLiveData()
    }


    fun selectBatsmanAndBowlerScores(list:List<PlayerScoreAndPlayer>){
        val playerIds = battingTeamWithPlayers.value?.playerList?.map {
            it.playerId
        }
        val batsmanAndScores = mutableListOf<PlayerScoreAndPlayer>()
        val bowlerAndScores = mutableListOf<PlayerScoreAndPlayer>()
        list.map {playerScoreAndPlayer->
            if (playerIds != null) {
                if(playerIds.contains(playerScoreAndPlayer.player.playerId)){
                    batsmanAndScores.add(playerScoreAndPlayer)
                }
                else{
                    bowlerAndScores.add(playerScoreAndPlayer)
                }
            }
        }
        _batsmanAndScores.value =batsmanAndScores
        _bowlerAndScores.value =bowlerAndScores
    }



    companion object {
        private const val NO_MATCH_ID = "NO_MATCH_ID"
        private const val MATCH_ID = "match_id"
    }
}