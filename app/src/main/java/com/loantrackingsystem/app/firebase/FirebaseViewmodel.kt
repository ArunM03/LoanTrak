package com.loantrackingsystem.app.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.loantrackingsystem.app.data.*
import com.loantrackingsystem.app.other.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirebaseViewmodel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val usersCollection = firestore.collection("users")
    private val loansCollection = firestore.collection("loans")

    private var _userCreatedLive = MutableLiveData<UserDataModel>()
    var userCreatedLive: LiveData<UserDataModel> = _userCreatedLive
    private var _errorUserCreatedLive = MutableLiveData<String>()
    var errorUserCreatedLive: LiveData<String> = _errorUserCreatedLive

    fun createUser(userData: UserDataModel) = viewModelScope.launch(Dispatchers.IO) {

        try {
            val users = usersCollection.whereEqualTo("username",userData.username).get().await().toObjects(UserDataModel::class.java)

            if(users.isEmpty()){

                usersCollection.document(userData.userId).set(userData).addOnSuccessListener {
                    _userCreatedLive.postValue(userData)
                }.addOnFailureListener {
                    _errorUserCreatedLive.postValue(it.message)
                }

            }else{

                _errorUserCreatedLive.postValue("Username already taken")

            }

        } catch (e: Exception) {

            _errorUserCreatedLive.postValue(e.message)

        }

    }

    private var _loanDataLive = MutableLiveData<LoanDataModel>()
    var loanDataLive: LiveData<LoanDataModel> = _loanDataLive
    private var _errorLoanDataLive = MutableLiveData<String>()
    var errorLoanDataLive: LiveData<String> = _errorLoanDataLive

    fun getLoanData(loanId : String) = viewModelScope.launch(Dispatchers.IO) {

        try {
            val loans = loansCollection.whereEqualTo("loanID",loanId).get().await().toObjects(LoanDataModel::class.java)

            if(loans.isNotEmpty()){
                _loanDataLive.postValue(loans[0])
            }else{
                _errorLoanDataLive.postValue("Empty")
            }

        } catch (e: Exception) {
            _errorLoanDataLive.postValue(e.message)
        }

    }

    private var _userDataLive = MutableLiveData<UserDataModel>()
    var userDataLive: LiveData<UserDataModel> = _userDataLive
    private var _errorUserDataLive = MutableLiveData<String>()
    var errorUserDataLive: LiveData<String> = _errorUserDataLive

    fun getUserData(mobileNumber : String) = viewModelScope.launch(Dispatchers.IO) {

        try {
            val users = usersCollection.whereEqualTo("phoneNumber",mobileNumber).get().await().toObjects(UserDataModel::class.java)

            if(users.isNotEmpty()){
                _userDataLive.postValue(users[0])
            }else{
                _errorUserDataLive.postValue("Empty")
            }

        } catch (e: Exception) {
            _errorUserDataLive.postValue(e.message)
        }

    }

    private var _userUpdatedLive = MutableLiveData<String>()
    var userUpdatedLive: LiveData<String> = _userUpdatedLive
    private var _errorUserUpdatedLive = MutableLiveData<String>()
    var errorUserUpdatedLive: LiveData<String> = _errorUserUpdatedLive

    fun updateUser(firstname : String, lastname : String, phoneNumber : String,userId: String,aadhar : String, phoneNumber2 : String,address : String,address2: String,
    address3: String,city : String,state : String,country : String,zipcode : String) = viewModelScope.launch(Dispatchers.IO) {

        try {

            usersCollection.document(userId).update(
                mapOf("firstName" to firstname,
                "secondName" to lastname,
                "phoneNumber" to phoneNumber,
                "phoneNumber2" to phoneNumber2,
                "aadhar" to aadhar,
                "address" to address,
                "address2" to address2,
                "address3" to address3,
                "city" to city,
                "state" to state,
                "country" to country,
                "zipcode" to zipcode)
            ).addOnSuccessListener {
                _userUpdatedLive.postValue("Success")
            }.addOnFailureListener {
                _errorUserUpdatedLive.postValue("Failure")
            }

        } catch (e: Exception) {

            _errorUserUpdatedLive.postValue(e.message)

        }

    }

    fun updateUserToken(token : String,userId: String) = viewModelScope.launch(Dispatchers.IO) {

        try {

            usersCollection.document(userId).update(
                "tokenId",token
            ).addOnSuccessListener {
                _userUpdatedLive.postValue(token)
            }.addOnFailureListener {
                _errorUserUpdatedLive.postValue("Failure")
            }

        } catch (e: Exception) {

            _errorUserUpdatedLive.postValue(e.message)

        }

    }



    private var _pinCreatedLive = MutableLiveData<UserDataModel>()
    var pinCreatedLive: LiveData<UserDataModel> = _pinCreatedLive
    private var _errorPinCreatedLive = MutableLiveData<String>()
    var errorPinCreatedLive: LiveData<String> = _errorPinCreatedLive

    fun updatePIN(userData: UserDataModel) = viewModelScope.launch(Dispatchers.IO) {

        try {

             usersCollection.document(userData.userId).update("pin",userData.pin).addOnSuccessListener {
                _pinCreatedLive.postValue(userData)
            }.addOnFailureListener {
                _errorPinCreatedLive.postValue(it.message)
            }

        } catch (e: Exception) {

            _errorPinCreatedLive.postValue(e.message)

        }

    }

    private var _notificationUpdatedLive = MutableLiveData<UserDataModel>()
    var notificationUpdatedLive: LiveData<UserDataModel> = _notificationUpdatedLive
    private var _errorNotificationUpdatedLive = MutableLiveData<String>()
    var errorNotificationUpdatedLive: LiveData<String> = _errorNotificationUpdatedLive

    fun updateNotification(userData: UserDataModel,notificationData : NotificationDataForUser) = viewModelScope.launch(Dispatchers.IO) {

        try {

            usersCollection.document(userData.userId).update(
                mapOf(
                    "pendingNotificationCount" to FieldValue.increment(1L),
                    "notifications" to FieldValue.arrayUnion(notificationData)
                )
            ).addOnSuccessListener {
                _notificationUpdatedLive.postValue(userData)
            }.addOnFailureListener {
                _errorNotificationUpdatedLive.postValue(it.message)
            }

        } catch (e: Exception) {

            _errorNotificationUpdatedLive.postValue(e.message)

        }

    }

    fun clearNotification(userData: UserDataModel) = viewModelScope.launch(Dispatchers.IO) {

        try {
            usersCollection.document(userData.userId).update(
                mapOf(
                    "pendingNotificationCount" to 0,
                )
            ).addOnSuccessListener {
                _notificationUpdatedLive.postValue(userData)
            }.addOnFailureListener {
                _errorNotificationUpdatedLive.postValue(it.message)
            }

        } catch (e: Exception) {

            _errorNotificationUpdatedLive.postValue(e.message)

        }

    }

    private var _userLoginLive = MutableLiveData<UserDataModel>()
    var userLoginLive: LiveData<UserDataModel> = _userLoginLive
    private var _errorUserLoginLive = MutableLiveData<String>()
    var errorUserLoginLive: LiveData<String> = _errorUserLoginLive

    fun loginUser(userName : String, password : String) = viewModelScope.launch(Dispatchers.IO) {

        try {

            val users = usersCollection.whereEqualTo("username",userName).whereEqualTo("password",password).get().await().toObjects(UserDataModel::class.java)

            if(users.isNotEmpty()){

                _userLoginLive.postValue(users[0])

            }else{

                _errorUserLoginLive.postValue("Login Failed, Invalid Username/password")

            }

        } catch (e: Exception) {

            _errorUserLoginLive.postValue(e.message)

        }

    }

    private var _addLoanLive = MutableLiveData<String>()
    var addLoanLive: LiveData<String> = _addLoanLive
    private var _errorAddLoanLive = MutableLiveData<String>()
    var errorAddLoanLive: LiveData<String> = _errorAddLoanLive

    fun addLoan(loanDataModel: LoanDataModel) = viewModelScope.launch(Dispatchers.IO) {

        try {
            loansCollection.document(loanDataModel.loanID).set(loanDataModel).addOnSuccessListener {
                _addLoanLive.postValue(loanDataModel.loanID)
            }.addOnFailureListener {
                _errorAddLoanLive.postValue(it.message)
            }
        } catch (e: Exception) {
            _errorAddLoanLive.postValue(e.message)
        }

    }

    private var _deleteLoanLive = MutableLiveData<String>()
    var deleteLoanLive: LiveData<String> = _deleteLoanLive
    private var _errorDeleteLoanLive = MutableLiveData<String>()
    var errorDeleteLoanLive: LiveData<String> = _errorDeleteLoanLive

    fun deleteLoan(loanDataModel: LoanDataModel) = viewModelScope.launch(Dispatchers.IO) {

        try {
            loansCollection.document(loanDataModel.loanID).delete().addOnSuccessListener {
                _deleteLoanLive.postValue("Success")
            }.addOnFailureListener {
                _errorDeleteLoanLive.postValue(it.message)
            }
        } catch (e: Exception) {
            _errorDeleteLoanLive.postValue(e.message)
        }

    }

    private var _addLoanPersonLive = MutableLiveData<String>()
    var addLoanPersonLive: LiveData<String> = _addLoanPersonLive
    private var _errorAddLoanPersonLive = MutableLiveData<String>()
    var errorAddLoanPersonLive: LiveData<String> = _errorAddLoanPersonLive

    fun addLoanPerson(loanPersonDataModel: LoanPersonDataModel,userId : String) = viewModelScope.launch(Dispatchers.IO) {

        try {
            usersCollection.document(userId).collection("loanpersons").document(loanPersonDataModel.loanPersonId).set(loanPersonDataModel).addOnSuccessListener {
                _addLoanPersonLive.postValue("Success")
            }.addOnFailureListener {
                _errorAddLoanPersonLive.postValue(it.message)
            }
        } catch (e: Exception) {
            _errorAddLoanPersonLive.postValue(e.message)
        }

    }


    private var _getLoanPersonLive = MutableLiveData<List<LoanPersonDataModel>>()
    var getLoanPersonLive: LiveData<List<LoanPersonDataModel>> = _getLoanPersonLive
    private var _errorGetLoanPersonLive = MutableLiveData<String>()
    var errorGetLoanPersonLive: LiveData<String> = _errorGetLoanPersonLive

    fun getLoanPerson(userId : String) = viewModelScope.launch(Dispatchers.IO) {

        try {
           val loanPersons = usersCollection.document(userId).collection("loanpersons").get().await().toObjects(LoanPersonDataModel::class.java)

           if(loanPersons.isNotEmpty()){
               _getLoanPersonLive.postValue(loanPersons)
           }else{
               _getLoanPersonLive.postValue(listOf())
           }

        } catch (e: Exception) {
            _errorGetLoanPersonLive.postValue(e.message)
        }

    }


    private var _getLoansLive = MutableLiveData<List<LoanDataModel>>()
    var getLoansLive: LiveData<List<LoanDataModel>> = _getLoansLive
    private var _errorGetLoansLive = MutableLiveData<String>()
    var errorGetLoansLive: LiveData<String> = _errorGetLoansLive

    fun getLoans(userId : String,type : String,phoneNumber : String) = viewModelScope.launch(Dispatchers.IO) {

        try {

            val loans= if(type.isNotEmpty()){
                if(type == Constants.LOANGIVEN){
                    loansCollection.whereEqualTo("loanGiverPhone",phoneNumber).whereEqualTo("status",Constants.NOT_PAID).get().await().toObjects(LoanDataModel::class.java)
                }else{
                    loansCollection.whereEqualTo("phone",phoneNumber).whereEqualTo("status",Constants.NOT_PAID).get().await().toObjects(LoanDataModel::class.java)
                }
            }else{
                loansCollection.whereArrayContains("usersPhone",phoneNumber).whereEqualTo("status",Constants.PAID).get().await().toObjects(LoanDataModel::class.java)
            }

            if(loans.isNotEmpty()){
                _getLoansLive.postValue(loans)
            }else{
                _getLoansLive.postValue(listOf())
            }


        } catch (e: Exception) {
            _errorGetLoansLive.postValue(e.message)
        }

    }


}