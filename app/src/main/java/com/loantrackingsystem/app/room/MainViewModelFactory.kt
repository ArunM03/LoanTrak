package com.loantrackingsystem.app.room

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class MainViewModelFactory(val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewmodel::class.java)){
            return MainViewmodel(application) as T
        }
        throw IllegalArgumentException("Viewmodel class not found")
    }

}