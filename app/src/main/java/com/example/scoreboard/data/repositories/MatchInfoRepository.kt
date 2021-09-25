package com.example.scoreboard.data.repositories

import com.example.scoreboard.data.dao.MatchDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchInfoRepository @Inject constructor(
    private val matchDao: MatchDao
)  {
    //fun getMatches()=matchDao.getMatches()
}