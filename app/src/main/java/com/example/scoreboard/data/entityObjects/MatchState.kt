package com.example.scoreboard.data.entityObjects

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "matchState")
data class MatchState(
    @PrimaryKey
    val matchId:String,
    val run :Int =0,
    var extra:Int =0,
    val run_bat : Int=0,
    val ballCount : Boolean =false,
    var bat :Boolean =false,
    var nb : Boolean=false,
    var wide :Boolean =false,
    var bye :Boolean =false,
    var lb :Boolean =false,
){

    fun getScoreString():String {
        var scoreString : String =(run+extra+run_bat).toString()
        if (bye && !nb){
            scoreString = "Bye-$scoreString"
        }
        if (lb && !nb){
            scoreString = "lb-$scoreString"
        }

        if (wide){
            scoreString = "wide-$scoreString"
        }

        if (nb){
            if (lb){
                scoreString = "nb&lb-$scoreString"
            }
            else if (bye){
                scoreString = "nb&bye-$scoreString"
            }
            else{
                scoreString = "nb-$scoreString"
            }
        }
        return scoreString
    }

   // override fun toString() = name
}


