package com.loantrackingsystem.app.other

import android.content.Context
import com.google.gson.Gson
import com.loantrackingsystem.app.data.UserData
import com.loantrackingsystem.app.data.UserDataModel


class SharedPref(context: Context) {

    val sharedpref = context.getSharedPreferences("loan_tracking_pref", Context.MODE_PRIVATE)
    val editor = sharedpref.edit()

    val USERDATA = "userdata"
    val USERLOGIN = "userlogin"
    val CURLANGUAGE = "CurLanguage"
    val CURCURRENCY = "Currency"
    val SETPIN = "SetPin"
    val NotificationCount = "NOTIFICATIONCOUNT"

    fun setUserData(userData: UserData){
        val gson =  Gson()
        val json = gson.toJson(userData)
        editor.putString(USERDATA, json)
        editor.putBoolean(USERLOGIN,true)
        editor.commit()
    }

    fun setUserDataModel(userData: UserDataModel){
        val gson =  Gson()
        val json = gson.toJson(userData)
        editor.putString(USERDATA, json)
        editor.putBoolean(USERLOGIN,true)
        editor.commit()
    }

    fun setPin(){
        editor.apply {
            putBoolean(SETPIN,true)
            apply()
        }
    }

    fun setNotifciations(count : Int){
        editor.apply {
            putInt(NotificationCount,count)
            apply()
        }
    }

    fun getNotifications() : Int{
        return sharedpref.getInt(NotificationCount,0)
    }

    fun getPin() : Boolean{
        return sharedpref.getBoolean(SETPIN,false)
    }

    fun setUserLoginStatus() {
        editor.apply {
            putBoolean(USERLOGIN,false)
            apply()
        }
    }

    fun getUserLoginStatus() : Boolean{
        return sharedpref.getBoolean(USERLOGIN,false)
    }

    fun getUserData() : UserData{
        val gson = Gson()
        val json = sharedpref.getString(USERDATA, "null")
        return gson.fromJson(json, UserData::class.java)
    }


    fun getUserDataModel() : UserDataModel{
        val gson = Gson()
        val json = sharedpref.getString(USERDATA, "null")
        return gson.fromJson(json, UserDataModel::class.java)
    }


    fun setLanguage(language: String) {
        editor.apply {
            putString(CURLANGUAGE, language)
            apply()
        }
    }

    fun getLanguage(): String? {
        return sharedpref.getString(CURLANGUAGE, "null")
    }


    fun setCurrency(language: String) {
        editor.apply {
            putString(CURCURRENCY, language)
            apply()
        }
    }

    fun getCurrency(): String? {
        return sharedpref.getString(CURCURRENCY, "null")
    }



}