package com.loantrackingsystem.app.firebase

import com.loantrackingsystem.app.firebase.NotificationData

data class PushNotification(
    val data: NotificationData,
    val to: String
)