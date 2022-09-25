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
    var username : String = "",
    var loanGiverPhone : String = "",
    var usersPhone : List<String>  = listOf(),
    var loanID : String = "",
    var emiData : List<LoanEmiData> = listOf(),
    var loanCreator : String = "",
    var isInReview : String = "",
    var secondPersonStatus : String = "",
    var secondPersonComment : String = "",
    var paymentType : String = "",
    var adhocEmiDataList : List<AdhocEmiData> = listOf()
)
data class LoanEmiData(
    var index : Int = 0,
    val date : String = "",
    var status : String = "",
    var amount : String = ""
)
data class AdhocEmiData(
    var index : Int = 0,
    val date : String = "",
    var paidPrincipleAmount : String = "",
    var paidInterestAmount : String = "",
    var remainingPrincipleAmount : String = "",
    var remainingInterestAmount : String = ""
)
const val ADHOC = "Ad-hoc"
const val MONTHLY = "Monthly"