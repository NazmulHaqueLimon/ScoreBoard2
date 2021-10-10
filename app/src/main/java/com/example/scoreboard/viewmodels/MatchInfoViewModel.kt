package com.example.scoreboard.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.scoreboard.data.objects.Player
import com.example.scoreboard.data.repositories.MatchInfoRepository
import com.example.scoreboard.data.repositories.NewMatchRepository
import com.example.scoreboard.data.repositories.ScoringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MatchInfoViewModel @Inject internal constructor(

    private val repository: ScoringRepository,
    private val savedStateHandle: SavedStateHandle

): ViewModel() {

    val players : LiveData<List<Player>> =repository.getPlayers().asLiveData()
}