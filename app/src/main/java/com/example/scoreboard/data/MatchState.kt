package com.example.scoreboard.data

data class MatchState(
    val run :Int =0,
    val extra:Int =0,
    val run_bat : Int=0,
    val ballCount : Boolean =false,
    val bat :Boolean =false,
    val nb : Boolean=false,
    val wide :Boolean =false,
    val bye :Boolean =false,
    val lb :Boolean =false,
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


