package com.loantrackingsystem.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "userdata_table")
data class UserDataModel(
    var userId : String = "",
    var username : String = "",
    var password : String = "",
    var pin : String = "",
    var phoneNumber : String = "",
    var firstName : String = "",
    var secondName : String = "",
    var tokenId : String = "",
    var pendingNotificationCount : Int = 0,
    var notifications : List<NotificationDataForUser> = listOf(),
    var phoneNumber2 : String = "",
    var aadhar : String = "",
    var address : String = "",
    var address2 : String = "",
    var address3 : String = "",
    var city : String = "",
    var state : String = "",
    var country : String = "",
    var zipcode : String = ""
)