package com.loantrackingsystem.app.ui.loan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.loantrackingsystem.app.R
import com.loantrackingsystem.app.databinding.FragmentAddloanBinding
import com.loantrackingsystem.app.other.Constants
import java.util.*
import android.database.Cursor
import android.net.Uri
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import android.annotation.SuppressLint


import android.telephony.SmsManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.loantrackingsystem.app.TOPIC
import com.loantrackingsystem.app.data.*

import com.loantrackingsystem.app.firebase.FirebaseViewmodel
import com.loantrackingsystem.app.firebase.NotificationData
import com.loantrackingsystem.app.firebase.PushNotification
import com.loantrackingsystem.app.firebase.RetrofitInstance
import com.loantrackingsystem.app.other.MyDialog
import com.loantrackingsystem.app.other.SharedPref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


const val PICK_CONTACT = 11

class AddLoanFragment : Fragment(R.layout.fragment_addloan) {

    lateinit var binding : FragmentAddloanBinding
   // lateinit var mainViewModel: MainViewmodel
    lateinit var mainViewModel: FirebaseViewmodel
    var date = ""
    var number = ""
    var userId = ""
    var username = ""
    var userphno = ""
    var endDate = "0L"
    var isNew = false
    lateinit var loanPersons : MutableList<String>
    lateinit var loanPersonsData : MutableList<LoanPersonDataModel>
    lateinit var myDialog: MyDialog
    var tokenId = ""
    var month = 0
    var year = 0
    var secondPersonDataModel = UserDataModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddloanBinding.bind(view)

        val sharedPref = SharedPref(requireContext())

    /*    val userData = sharedPref.getUserData()
        username = userData.username*/

        val userData = sharedPref.getUserDataModel()
        userId = userData.userId
        username = userData.firstName
        userphno = userData.phoneNumber



        setUI(view)

    }

    private fun setUI(view: View) {

/*        val mainViewModelFactory = MainViewModelFactory(requireActivity().application)

        mainViewModel =
            ViewModelProvider(this, mainViewModelFactory).get(MainViewmodel::class.java)*/

        mainViewModel =
            ViewModelProvider(this).get(
                FirebaseViewmodel::class.java)

        myDialog = MyDialog(requireContext())

        Constants.requestPermissions(requireActivity())

        date = Calendar.getInstance().timeInMillis.toString()

        loanPersons = mutableListOf<String>(getString(R.string.selectperson))

        binding.cdAddloan.setOnClickListener {

            if (Constants.hasSMSPermissions(requireContext())) {

                val name = binding.edName.selectedItem.toString()
                val phone = binding.edPhonenumber.text.toString()
                val amount = binding.edAmount.text.toString()
                val interest = binding.edInterest.text.toString()
                val emi = binding.edEmi.text.toString()
                val description = binding.edDescription.selectedItem.toString()
                val otherreason = binding.edOthereasons.text.toString()
                val loanType = binding.edLoanType.selectedItem.toString()
                val paymentType = binding.edPaymenttype.selectedItem.toString()

                val status = Constants.NOT_PAID


                if (name != getString(R.string.selectperson) && interest.isNotEmpty() && emi.isNotEmpty() && amount.isNotEmpty() && description.isNotEmpty() && date.isNotEmpty()) {

                    if(interest.toFloat() > 100f){
                        Toast.makeText(requireContext(), "Interest should be between 0 to 100", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    val loanId = UUID.randomUUID().toString().substring(0,15)

                    if(description == "Other" && otherreason.isEmpty()){
                        Toast.makeText(requireContext(), "Please enter other reason", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    val reason = if(description == "Other"){
                       otherreason
                    }else{
                        description
                    }

                    val loanData = if(loanType == "Loan Given") {
                        LoanDataModel(
                            name,
                            phone,
                            amount,
                            reason,
                            date,
                            endDate,
                            emi.toFloat(),
                            interest.toFloat(),
                            status,
                            binding.edMonthstxt.text.toString(),
                            binding.edYrtext.text.toString(),
                            userId,
                            loanType,
                            username,
                            userphno,
                            listOf<String>(userphno,phone),
                            loanId,
                            listOf(),
                            userId,
                            Constants.NO,
                            "",
                            "",
                            paymentType
                        )
                    }else {
                        LoanDataModel(
                            username,
                            userphno,
                            amount,
                            reason,
                            date,
                            endDate,
                            emi.toFloat(),
                            interest.toFloat(),
                            status,
                            binding.edMonthstxt.text.toString(),
                            binding.edYrtext.text.toString(),
                            userId,
                            loanType,
                            name,
                            phone,
                            listOf<String>(userphno,phone),
                            loanId,
                            listOf(),
                            userId,
                            Constants.NO,
                            "",
                            "",
                            paymentType
                        )
                    }

                    mainViewModel.addLoan(loanData)

            /*        mainViewModel.addLoan(
                        LoanData(
                            name,
                            phone,
                            amount,
                            description,
                            date,
                            endDate,
                            emi.toFloat(),
                            interest.toFloat(),
                            status,
                            binding.edMonths.selectedItem.toString(),
                            binding.edYrs.selectedItem.toString(),
                            username
                        )
                    )*/

                    if (isNew) {
                        val loanPersonId = UUID.randomUUID().toString().substring(0,15)
                   //     mainViewModel.addLoanPerson(LoanPersonData(name, phone,userId))
                        mainViewModel.addLoanPerson(LoanPersonDataModel(name, phone,loanPersonId),userId)
                    }


                    myDialog.showProgressDialog("Adding...Please wait",this)
                    //sendSMS("9384331746")


                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.pleaseenteralldetails),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }else{
               Constants.requestPermissions(requireActivity())
            }

        }


        binding.tvDateValue.text = getDate(Calendar.getInstance().timeInMillis)

        binding.tvDateValue.setOnClickListener {

            showDatePickerDialog()

        }

        setSpinner(binding.edMonths, listOf("0","1","2","3","4","5","6","7","8","9","10","11"))
        setSpinner(binding.edYrs, listOf("0","1","2","3","4","5","6","7","8","9","10"))
        setSpinner(binding.edPaymenttype, listOf(ADHOC, MONTHLY))

        setSpinner(binding.edLoanType, listOf("Loan Given","Loan Taken"))

        setSpinner(binding.edDescription, listOf("Select Reason","Car Loan","Hand Loan","Personal Loan","Education Loan","Home renovation Loan","Other"),isReason = true)

        binding.ibContact.setOnClickListener {
           val intent = Intent(Intent.ACTION_PICK)
            intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            startActivityForResult(intent, PICK_CONTACT)
        }

/*        mainViewModel.getAllLoanPerson(userId).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            loanPersonsData = it.toMutableList()
            loanPersons.addAll(getNames(it))
            setSpinner(binding.edName,loanPersons,true)
        })*/


        mainViewModel.addLoanLive.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            myDialog.dismissProgressDialog()

          //  sendNotification(PushNotification(NotificationData(username,"your new loan request is initiated, review it!"), tokenId))

          //  mainViewModel.updateNotification(secondPersonDataModel, NotificationDataForUser(Calendar.getInstance().timeInMillis.toString(),"$username created new loan",it))

            sendSMS(number)

            Toast.makeText(requireContext(), getString(R.string.added), Toast.LENGTH_SHORT)
                .show()

            requireActivity().onBackPressed()

        })

        mainViewModel.getLoanPerson(userId)

        mainViewModel.getLoanPersonLive.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            loanPersonsData = it.toMutableList()
            loanPersons.addAll(getNames2(it))
            setSpinner(binding.edName,loanPersons,true)
        })

        mainViewModel.errorAddLoanLive.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            myDialog.dismissProgressDialog()

            myDialog.showErrorAlertDialog(it)

        })



        mainViewModel.userDataLive.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            secondPersonDataModel = it
            tokenId = it.tokenId
        })


        /*    binding.tvEnddateValue.setOnClickListener {

                  showDatePickerDialog(true)

              }*/

        binding.edInterest.doAfterTextChanged {

            it?.let {
                if(binding.edInterest.hasFocus()){
                    val amount = binding.edAmount.text.toString()
                    if(amount.isNotEmpty() && it.isNotEmpty()){
                        val emi = amount.toInt() * (it.toString().toFloat()/100)
                        binding.edEmi.setText(emi.toString())
                    }
                }

            }

        }


        binding.edEmi.doAfterTextChanged {

            it?.let {
                if(binding.edEmi.hasFocus()){
                    val amount = binding.edAmount.text.toString()
                    if(amount.isNotEmpty() && it.isNotEmpty()){
                        val emi = ((it.toString().toFloat()) / amount.toInt()) * 100
                        binding.edInterest.setText(emi.toString())
                    }

                }
            }

        }

        binding.tvAddyr.setOnClickListener {
            year++
            binding.edYrtext.text = year.toString()
        }

        binding.tvAddmth.setOnClickListener {
            month++
            if (month > 11){
                month = 0
                year++
            }
            binding.edMonthstxt.text = month.toString()
            binding.edYrtext.text = year.toString()
        }

        binding.tvSubyr.setOnClickListener {
            if (year>0){
                year--
                binding.edYrtext.text = year.toString()
            }
        }

        binding.tvSubmth.setOnClickListener {
            if (month>0){
            month--
            binding.edMonthstxt.text = month.toString()
            binding.edYrtext.text = year.toString()
            }

        }


    }

    fun showDatePickerDialog(isEndDate : Boolean = false){

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.selectdate))
                .setSelection(Calendar.getInstance().timeInMillis)
                .build()
        datePicker.addOnPositiveButtonClickListener {
            val yearFormat = SimpleDateFormat("MMM dd, yyyy")
            val timeText = yearFormat.format(it)
            if(isEndDate){
      /*          binding.tvEnddateValue.text = timeText.toString()
                endDate = it.toString()*/
            }else{
                binding.tvDateValue.text = timeText.toString()
                date = it.toString()
            }

        }
        datePicker.show(childFragmentManager,"Match")

    }

    fun getNames(loanPersonData: List<LoanPersonData>) : MutableList<String>{

        val names = mutableListOf<String>()
        for (person in loanPersonData){
            names.add(person.name)
        }
        return names
    }

    fun getNames2(loanPersonDatamodel : List<LoanPersonDataModel>) : MutableList<String>{

        val names = mutableListOf<String>()
        for (person in loanPersonDatamodel){
            names.add(person.name)
        }
        return names
    }

    fun setSpinner(spinner: Spinner, spinnerList : List<String>,isName : Boolean = false,isReason : Boolean = false) {
        val adapter = object :
            ArrayAdapter<Any>(
                requireContext(), R.layout.sp_layout,
                spinnerList
            ) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                return super.getDropDownView(position, convertView, parent).also {
                    if (position == spinner.selectedItemPosition) {
                        it.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.blue_100))
                    }
                }
            }
        }
        adapter.setDropDownViewResource(R.layout.sp_layout)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {



            }

            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                if(isReason) {
                    if (spinner.selectedItem.toString() == "Other"){
                        binding.edOthereasons.visibility = View.VISIBLE
                    }else{
                        binding.edOthereasons.visibility = View.GONE
                    }
                }

                if(isName){
                    val loanPersonData  = loanPersonsData.filter { it.name == spinner.selectedItem.toString() }
               //     Toast.makeText(requireContext(), "${spinner.selectedItem.toString()} and ${loanPersonData}", Toast.LENGTH_SHORT).show()
                    if(loanPersonData.isNotEmpty()){
                        binding.edPhonenumber.text = loanPersonData[0].phone
                        mainViewModel.getUserData(loanPersonData[0].phone)
                        number = loanPersonData[0].phone
                    }
                }

            }
        }
    }

    @SuppressLint("Range", "Recycle")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(PICK_CONTACT == requestCode){

            if(resultCode == Activity.RESULT_OK){

                val contactData: Uri = data?.data ?: return
                val cols = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME )
                val c: Cursor? = requireActivity().contentResolver.query(contactData,cols,null,null,null)

                if(c?.moveToFirst()!!){

                    val num = c.getString(0)
                    val name = c.getString(1)

                   if(!loanPersons.contains(name)){
                       loanPersons.add(name)
                       isNew = true
                   }else{
                       isNew = false
                   }
                    val index = loanPersons.indexOf(name)

                    setSpinner(binding.edName,loanPersons)

                    binding.edName.setSelection(index)

                    val number2 = num

                    number = number2
                    binding.edPhonenumber.setText(number2)
                    mainViewModel.getUserData(number2)

                   // sendSMS(number)
                }

            }

        }

    }

    fun sendSMS(number2 : String){

        val number = number2.replace(" ","").takeLast(10)

        //Getting intent and PendingIntent instance

      //  Getting intent and PendingIntent instance
/*        val intent = Intent(
            requireContext(),
            MainActivity::class.java
        )

        val pi = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
               requireContext(),
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                requireContext(),
                0,
                intent,
                0
            )
        }*/
        //Get the SmsManager instance and call the sendTextMessage method to send message


    //    try {
            //Get the SmsManager instance and call the sendTextMessage method to send message
          //  val sms: SmsManager = SmsManager.getDefault()
            val sms: SmsManager = SmsManager.getDefault()
           // sms.sendTextMessage(number2, null, "$name invited you to Loan Tracking App. Please install this app using the following link https://play.google.com/store/apps/details?id=com.loantrackingsystem.app", pi, null)
            sms.sendTextMessage(number, null, "$username invited you to Loan Tracking App. https://play.google.com/store/apps/details?id=com.loantrackingsystem.app", null, null)

   /*     }catch (e:Exception){
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
        }*/

    }
//You have been invited to Loan Tracking App by $name  to track loan. Please install app using the following link https://play.google.com/store/apps/details?id=com.loantrackingsystem.app
}

fun Fragment.sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {

    try {
        val response = RetrofitInstance.api.postNotification(notification)
        if(response.isSuccessful) {
            withContext(Dispatchers.Main) {
          //      Toast.makeText(requireContext(), "Successful", Toast.LENGTH_SHORT).show()
            }
            //  Log.d(TAG, "Response: ${Gson().toJson(response)}")
        } else {
            withContext(Dispatchers.Main) {
             //   Toast.makeText(requireContext(), "Unsuccessful", Toast.LENGTH_SHORT).show()
            }
            //    Log.e(TAG, response.errorBody().toString())
        }
    } catch(e: Exception) {
        withContext(Dispatchers.Main){
          //  Toast.makeText(requireContext(),"Something went wrong ${e.message}",Toast.LENGTH_SHORT).show()
        }
        // Log.e(TAG, e.toString())
    }
}
