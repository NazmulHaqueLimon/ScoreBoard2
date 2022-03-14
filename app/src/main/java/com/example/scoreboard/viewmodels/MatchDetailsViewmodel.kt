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


    fun setMatchId(id:String){
        matchId.value =id
    }

    /**
     * matchId...need to save in savedStateHandle*/
    private val matchId: MutableStateFlow<String> = MutableStateFlow(
        savedState.get(MATCH_ID) ?:NO_MATCH_ID
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
        scoringRepo.getMatch(it)

    }.asLiveData()

    /**
     * get the teams from the database
     * */
    @ExperimentalCoroutinesApi
    val teamA = match.switchMap { match ->
        scoringRepo.getTeamWithPlayers(match.teamA_id).asLiveData()
    }

    @ExperimentalCoroutinesApi
    val teamB = match.switchMap { match ->
        scoringRepo.getTeamWithPlayers(match.teamB_id).asLiveData()
    }

    @ExperimentalCoroutinesApi
    val teamAplayers: LiveData<List<Player>> = Transformations.map(teamA) {
        it.playerList
    }
    @ExperimentalCoroutinesApi
    val teamBplayers: LiveData<List<Player>> = Transformations.map(teamB) {
        it.playerList

    }


    @ExperimentalCoroutinesApi
    val matchPlayerScoreAndPlayers =matchId.flatMapLatest {
        repository.getMatchPlayerScorePlayer(it)
    }.asLiveData()


    @ExperimentalCoroutinesApi
    val matchTeamScoreTeam = matchId.flatMapLatest {
        repository.getMatchTeamScoreTeam(it)
    }.asLiveData()



    val _battingTeamAndScore = MutableLiveData<TeamScoreAndTeam>()
    val battingTeamAndScore :LiveData<TeamScoreAndTeam> =_battingTeamAndScore

    val _bowlingTeamAndScore = MutableLiveData<TeamScoreAndTeam>()
    val bowlingTeamAndScore :LiveData<TeamScoreAndTeam> =_bowlingTeamAndScore


    val _battingTeamWithPlayers = MutableLiveData<TeamWithPlayers>()
    val battingTeamWithPlayers:LiveData<TeamWithPlayers> =_battingTeamWithPlayers
    val _bowlingTeamWithPlayers= MutableLiveData<TeamWithPlayers>()
    val bowlingTeamWithPlayers:LiveData<TeamWithPlayers> =_bowlingTeamWithPlayers






    private val _batsmanwithScoreList= MutableLiveData<List<PlayerScoreAndPlayer>>()
    val batsmanwithScoreList:LiveData<List<PlayerScoreAndPlayer>> =_batsmanwithScoreList

    private val _bowlerWithScoreList= MutableLiveData<List<PlayerScoreAndPlayer>>()
    val bowlerWithScoreList:LiveData<List<PlayerScoreAndPlayer>> =_bowlerWithScoreList




    companion object {
        private const val NO_MATCH_ID = "NO_MATCH_ID"
        private const val MATCH_ID = "match_id"
    }
}