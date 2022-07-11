package com.loantrackingsystem.app.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.loantrackingsystem.app.data.LoanData
import com.loantrackingsystem.app.data.LoanPersonData
import com.loantrackingsystem.app.data.UserData


@Dao
interface LoanDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(userData: UserData)


    @Update
    suspend fun updateUserData(userData: UserData)

    @Delete
    suspend fun deleteUser(userData: UserData)

    @Query("SELECT * FROM userdata_table")
    fun getAllUsers() : LiveData<List<UserData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLoan(loanData: LoanData)

    @Delete
    suspend fun deleteLoan(loanData: LoanData)

    @Update
    suspend fun updateLoan(loanData: LoanData)

    @Query("SELECT * FROM loan_table  where userid = :name")
    fun getAllLoans(name : String) : LiveData<List<LoanData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLoanPerson(loanPersonData: LoanPersonData)

    @Query("SELECT * FROM loanperson_table where username = :name")
    fun getAllLoanPersons(name : String) : LiveData<List<LoanPersonData>>

}