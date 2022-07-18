package com.loantrackingsystem.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey


data class LoanPersonDataModel(
    var name : String = "",
    var phone : String = "",
    var loanPersonId : String = ""
)