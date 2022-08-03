package com.loantrackingsystem.app.other

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import com.loantrackingsystem.app.data.LoanData
import com.loantrackingsystem.app.data.LoanDataModel
import com.loantrackingsystem.app.data.UserData
import com.loantrackingsystem.app.data.UserDataModel
import pub.devrel.easypermissions.EasyPermissions

object Constants {


    var userData = UserData()
    var userDataModel = UserDataModel()

    var isLanguageChanged = false

    val PAID = "PAID"
    val NOT_PAID = "NOT PAID"

    val LOANGIVEN = "Loan Given"
    val LOANTAKEN = "Loan Taken"

    var isFirstTime = false

    var isSetPin = false


    var loanData = LoanDataModel()



    fun requestPermissions(activity_host : Activity) {
        if(Constants.hasSMSPermissions(activity_host)) {
            return
        }
            EasyPermissions.requestPermissions(
                activity_host,
                "You need to accept SMS send permission to use this app.",
                0,
                Manifest.permission.SEND_SMS
            )
    }

    fun hasSMSPermissions(context: Context) =
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.SEND_SMS
            )


}