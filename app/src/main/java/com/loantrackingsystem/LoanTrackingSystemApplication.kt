package com.loantrackingsystem

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.loantrackingsystem.app.R


class LoanTrackingSystemApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setTheme(R.style.Theme_LoanTrackingSystem)


    }

}