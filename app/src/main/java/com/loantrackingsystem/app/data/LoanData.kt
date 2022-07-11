package com.loantrackingsystem.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "loan_table")
data class LoanData(
    var name : String = "",
    var phone : String = "",
    var amount : String = "",
    var description : String = "",
    var date : String = "",
    var endDate : String = "",
    var emi : Float = 0f,
    var interest : Float = 0f,
    var status : String = "",
    var months : String = "",
    var years : String = "",
    var userid : String = "",
    @PrimaryKey(autoGenerate = true)
    var loanID : Int? = null
)