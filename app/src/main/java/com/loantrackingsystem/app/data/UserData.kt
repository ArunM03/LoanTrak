package com.loantrackingsystem.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "userdata_table")
data class UserData(
    var username : String = "",
    var password : String = "",
    var pin : String = "",
    @PrimaryKey(autoGenerate = true)
    var userID : Int? = null
)