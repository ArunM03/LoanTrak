package com.loantrackingsystem.app.room

import com.loantrackingsystem.app.data.LoanData
import com.loantrackingsystem.app.data.LoanPersonData
import com.loantrackingsystem.app.data.UserData
import com.loantrackingsystem.app.room.LoanDao



class LoanRepository(
 val loanDao: LoanDao
) {

    suspend fun addUser(userData: UserData) {
        loanDao.addUser(userData)
    }

    suspend fun deleteUser(userData: UserData) {
        loanDao.deleteUser(userData)
    }

    suspend fun updateUser(userData: UserData) {
        loanDao.updateUserData(userData)
    }

    val allUsers = loanDao.getAllUsers()

    suspend fun addLoan(loanData: LoanData) {
        loanDao.addLoan(loanData)
    }

    suspend fun updateLoan(loanData: LoanData) {
        loanDao.updateLoan(loanData)
    }

    suspend fun deleteLoan(loanData: LoanData) {
        loanDao.deleteLoan(loanData)
    }

    fun getAllLoans(name  : String) = loanDao.getAllLoans(name)

    suspend fun addLoanPerson(loanPersonData: LoanPersonData) {
        loanDao.addLoanPerson(loanPersonData)
    }

    fun getAllLoanPerson(name  : String) = loanDao.getAllLoanPersons(name)
}