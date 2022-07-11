    package com.loantrackingsystem.app.room

    import android.app.Application
    import androidx.lifecycle.AndroidViewModel
    import androidx.lifecycle.viewModelScope
    import com.loantrackingsystem.app.data.LoanData
    import com.loantrackingsystem.app.data.LoanPersonData
    import com.loantrackingsystem.app.data.UserData
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.launch

    class MainViewmodel(application: Application) : AndroidViewModel(application) {


        val loanDatabase = LoanDatabase.createLoanDatabase(application.applicationContext).loanDao()

        val loanRepository = LoanRepository(loanDatabase)

        fun addUser(userData: UserData) = viewModelScope.launch(Dispatchers.IO) {
            loanRepository.addUser(userData)
        }

        fun deleteUser(userData: UserData) = viewModelScope.launch(Dispatchers.IO) {
            loanRepository.deleteUser(userData)
        }

        fun updateUser(userData: UserData) = viewModelScope.launch(Dispatchers.IO) {
            loanRepository.updateUser(userData)
        }

        val allUsers = loanRepository.allUsers

        fun addLoan(loanData: LoanData) = viewModelScope.launch(Dispatchers.IO) {
            loanRepository.addLoan(loanData)
        }

        fun updateLoan(loanData: LoanData) = viewModelScope.launch(Dispatchers.IO) {
            loanRepository.updateLoan(loanData)
        }

        fun deleteLoan(loanData: LoanData) = viewModelScope.launch(Dispatchers.IO) {
            loanRepository.deleteLoan(loanData)
        }

        fun getAllLoan(name : String) = loanRepository.getAllLoans(name)

         fun addLoanPerson(loanPersonData: LoanPersonData)  = viewModelScope.launch(Dispatchers.IO) {
             loanRepository.addLoanPerson(loanPersonData)
        }

        fun getAllLoanPerson(name : String) = loanRepository.getAllLoanPerson(name)

    }