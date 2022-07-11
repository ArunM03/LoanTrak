package com.loantrackingsystem.app.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.loantrackingsystem.app.data.LoanData
import com.loantrackingsystem.app.data.LoanPersonData
import com.loantrackingsystem.app.data.UserData

@Database(entities = [UserData::class,LoanData::class,LoanPersonData::class],version = 1,exportSchema = false)
abstract class LoanDatabase : RoomDatabase() {

    abstract fun loanDao() : LoanDao

    companion object {
        @Volatile
        private var INSTANCE: LoanDatabase? = null
        fun createLoanDatabase(context: Context): LoanDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LoanDatabase::class.java,
                    "LoanDatabase"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}