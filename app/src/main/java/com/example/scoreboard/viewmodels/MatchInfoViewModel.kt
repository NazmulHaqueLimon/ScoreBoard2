package com.example.scoreboard.viewmodels

import androidx.lifecycle.*
import com.example.scoreboard.data.repositories.MatchInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MatchInfoViewModel @Inject internal constructor(

    private val repository: MatchInfoRepository,
    private val savedState: SavedStateHandle

): ViewModel() {

    val matchTeamScoreList = repository.getTeamMatchAndScore().asLiveData()


}