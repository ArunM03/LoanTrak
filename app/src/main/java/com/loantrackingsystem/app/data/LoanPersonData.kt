package com.loantrackingsystem.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loanperson_table")
data class LoanPersonData(
    var name : String = "",
    var phone : String = "",
    var username : String = "",
    @PrimaryKey(autoGenerate = true)
    var loanPersonId : Int? = null
)