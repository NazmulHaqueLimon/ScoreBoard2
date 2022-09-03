package com.example.scoreboard

import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import java.util.regex.Matcher
import java.util.regex.Pattern

object Utils {
    fun formatPhoneNumber(phoneNumber:String):String{
        return when {
            phoneNumber.startsWith("+880") -> phoneNumber
            phoneNumber.substring(0,1)=="0" -> "+88$phoneNumber"
            else -> "+880$phoneNumber"
        }
    }

    fun isPhoneNumberValid(number: String): Boolean {
        val pattern: Pattern = Pattern.compile( "((0|01|\\+88|\\+88\\s*\\(0\\)|\\+88\\s*0)\\s*)?1(\\s*[0-9]){9}")
        val matcher: Matcher = pattern.matcher(number)
        return matcher.matches()
    }



}