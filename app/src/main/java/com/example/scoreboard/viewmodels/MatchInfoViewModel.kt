package com.example.scoreboard.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.scoreboard.data.repositories.MatchInfoRepository
import com.example.scoreboard.data.repositories.NewMatchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MatchInfoViewModel @Inject internal constructor(

    plantRepository: MatchInfoRepository,
    private val savedStateHandle: SavedStateHandle

): ViewModel() {
}