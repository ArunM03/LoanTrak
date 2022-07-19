package com.loantrackingsystem.app.data


data class LoanDataModel(
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
    var loanType : String = "",
    var loanID : String = ""
)