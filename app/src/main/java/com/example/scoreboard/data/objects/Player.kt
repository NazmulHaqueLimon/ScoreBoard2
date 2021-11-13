package com.example.scoreboard.data.objects


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "players")
data class Player(
    @PrimaryKey
    val id : String = UUID.randomUUID().toString(),
    val name: String,
    var isOut : Boolean =false,
    var totalRun:Int=0,
    var totalWicket: Int=0

)
/**
 *

@ColumnInfo(name = "completed") var isCompleted = false

val titleForList: String
    get() = if (title.isNotEmpty()) title else description


val isActive
    get() = !isCompleted

val isEmpty
    get() = title.isEmpty() && description.isEmpty()
*/