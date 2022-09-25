package com.loantrackingsystem.app.other

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import com.loantrackingsystem.app.data.*
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
    var isPinChange = false


    var isProfile = false


    var loanData = LoanDataModel()

    var curLoanEMIDate = LoanEmiData()

    var isFromNotification = false
    var loanId = ""

    val YES = "Yes"
    val NO = "No"

    var notificationList = listOf<NotificationDataForUser>()

    const val BASE_URL = "https://fcm.googleapis.com"
    const val SERVER_KEY = "AAAAj9-LXKQ:APA91bFZYsrTddCmzmuvRskAsQtX6B3dnMF-tXOnWTC3-2gsYlH8JX72renKpGVbvyBkM0SvU-pMbg_irkO1YsGmDlo-hPIMC0zcmo9U-A0myD_lSl1IFZMyEWuyCT51-OQ4g5XAeaH5"
    const val CONTENT_TYPE = "application/json"


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