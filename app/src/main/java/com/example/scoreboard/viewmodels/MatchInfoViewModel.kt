package com.example.scoreboard.viewmodels

import androidx.lifecycle.*
import com.example.scoreboard.data.objects.*
import com.example.scoreboard.data.repositories.MatchInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchInfoViewModel @Inject internal constructor(

    private val repository: MatchInfoRepository,
    private val savedStateHandle: SavedStateHandle

): ViewModel() {

    private val matchList = repository.getMatches().asLiveData()

    private val matchTeamScoreList = repository.getTeamMatchAndScore().asLiveData()



    private fun getTeamScore(matchId: String, teamId: String): LiveData<TeamsScore> {
        return repository.getTeamScore(teamId,matchId).asLiveData()
    }

    private fun getTeam(id: String): Flow<Team> {
        return repository.getTeam(id)
    }

}