package com.loantrackingsystem.app.ui.loan

import android.os.Bundle
import android.service.autofill.UserData
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.loantrackingsystem.app.R
import com.loantrackingsystem.app.data.*

import com.loantrackingsystem.app.databinding.FragmentViewloanBinding
import com.loantrackingsystem.app.databinding.FragmentViewreviewBinding
import com.loantrackingsystem.app.firebase.FirebaseViewmodel
import com.loantrackingsystem.app.firebase.NotificationData
import com.loantrackingsystem.app.firebase.PushNotification
import com.loantrackingsystem.app.other.Constants
import com.loantrackingsystem.app.other.MyDialog
import com.loantrackingsystem.app.other.SharedPref
import java.util.*

class ViewReviewLoanFragment : Fragment(R.layout.fragment_viewreview) {


    lateinit var binding : FragmentViewreviewBinding
    lateinit var mainViewModel: FirebaseViewmodel
    lateinit var myDialog: MyDialog
    var date = ""
    lateinit var sharedPref : SharedPref
    var userPhone = ""
    var tokenId = ""
    var endDate = "0L"
    var message = ""
    var month = 0
    var year = 0
    lateinit var userDataModel: UserDataModel
    var secondPersonDataModel = UserDataModel()
    val loanReason = listOf("Car Loan","Hand Loan","Personal Loan","Education Loan","Home renovation Loan","Other")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentViewreviewBinding.bind(view)

        setUI(view)

    }

    private fun setUI(view: View) {

        mainViewModel =
            ViewModelProvider(this).get(
                FirebaseViewmodel::class.java)

        myDialog = MyDialog(requireContext())

        sharedPref = SharedPref(requireContext())

        userDataModel = sharedPref.getUserDataModel()

        if (Constants.isFromNotification){

          mainViewModel.getLoanData(Constants.loanId)

           mainViewModel.loanDataLive.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
               binding.progressbar.visibility = View.GONE
               binding.ctViewloandetails.visibility = View.VISIBLE
               Constants.loanData = it
               setData()
           })

            mainViewModel.errorLoanDataLive.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
            })

        }else{

            binding.progressbar.visibility = View.GONE
            binding.ctViewloandetails.visibility = View.VISIBLE

            setData()

        }


        setSpinner(binding.edDescription, loanReason,true)



        binding.cdAccept.setOnClickListener {
            mainViewModel.addLoan(Constants.loanData.apply {
                this.isInReview = Constants.NO
            })
            message = "Accepted the loan!"
            myDialog.showProgressDialog("Please wait",this)
        }

        binding.cdRequestforchange.setOnClickListener {
            val changes = binding.edRequestchange.text.toString()
            if (changes.isNotEmpty()){
                mainViewModel.addLoan(Constants.loanData.apply {
                    this.secondPersonComment = changes
                })
                message = "Requested for changes!"
                myDialog.showProgressDialog("Please wait",this)
            }else{
                Toast.makeText(requireContext(), "Change should not be empty", Toast.LENGTH_SHORT).show()
            }
        }

        binding.cdMadechanges.setOnClickListener {

            val name = binding.edName.text.toString()
            val phone = binding.edPhonenumber.text.toString()
            val amount = binding.edAmount.text.toString()
            val interest = binding.edInterest.text.toString()
            val emi = binding.edEmi.text.toString()
            val description = binding.edDescription.selectedItem.toString()
            val otherreason = binding.edOthereasons.text.toString()
            val loanType = binding.tvLoanvalue.text.toString()

            val status = if (binding.rbPaid.isChecked){
                Constants.PAID
            }else{
                Constants.NOT_PAID
            }

            if(name != getString(R.string.selectperson) && interest.isNotEmpty() && emi.isNotEmpty() && amount.isNotEmpty() && description.isNotEmpty() && date.isNotEmpty()){

                if(description == "Other" && otherreason.isEmpty()){
                    Toast.makeText(requireContext(), "Please enter other reason", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val reason = if(description == "Other"){
                    otherreason
                }else{
                    description
                }

                val loanData = Constants.loanData

                mainViewModel.addLoan(LoanDataModel(name,phone, amount,reason,date,endDate,emi.toFloat(),interest.toFloat(),
                    status,Constants.loanData.months,Constants.loanData.years,Constants.loanData.userid,loanType,Constants.loanData.username,
                    Constants.loanData.loanGiverPhone,Constants.loanData.usersPhone, Constants.loanData.loanID,
                    listOf(),loanData.loanCreator,loanData.isInReview,"${userDataModel.firstName} : Made Changes",""))

                myDialog.showProgressDialog("Updating...Please wait",this)

            }else{

                Toast.makeText(requireContext(), getString(R.string.pleaseenteralldetails), Toast.LENGTH_SHORT).show()

            }

        }

        binding.cdCantmakechanges.setOnClickListener {

            val name = binding.edName.text.toString()
            val phone = binding.edPhonenumber.text.toString()
            val amount = binding.edAmount.text.toString()
            val interest = binding.edInterest.text.toString()
            val emi = binding.edEmi.text.toString()
            val description = binding.edDescription.selectedItem.toString()
            val otherreason = binding.edOthereasons.text.toString()
            val loanType = binding.tvLoanvalue.text.toString()

            val status = if (binding.rbPaid.isChecked){
                Constants.PAID
            }else{
                Constants.NOT_PAID
            }

            if(name != getString(R.string.selectperson) && interest.isNotEmpty() && emi.isNotEmpty() && amount.isNotEmpty() && description.isNotEmpty() && date.isNotEmpty()){

                if(description == "Other" && otherreason.isEmpty()){
                    Toast.makeText(requireContext(), "Please enter other reason", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val reason = if(description == "Other"){
                    otherreason
                }else{
                    description
                }

                val loanData = Constants.loanData

                mainViewModel.addLoan(LoanDataModel(name,phone, amount,reason,date,endDate,emi.toFloat(),interest.toFloat(),
                    status,Constants.loanData.months,Constants.loanData.years,Constants.loanData.userid,loanType,Constants.loanData.username,
                    Constants.loanData.loanGiverPhone,Constants.loanData.usersPhone, Constants.loanData.loanID,
                    listOf(),loanData.loanCreator,loanData.isInReview,"${userDataModel.firstName} : Cannot Make Changes",""))

                myDialog.showProgressDialog("Updating...Please wait",this)

            }else{

                Toast.makeText(requireContext(), getString(R.string.pleaseenteralldetails), Toast.LENGTH_SHORT).show()

            }

        }



        mainViewModel.addLoanLive.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            myDialog.dismissProgressDialog()

            if (binding.cdAccept.visibility == View.VISIBLE){
                sendNotification(PushNotification(NotificationData(userDataModel.firstName,message),tokenId))
                mainViewModel.updateNotification(secondPersonDataModel, NotificationDataForUser(
                    Calendar.getInstance().timeInMillis.toString(),message,it)
                )
            }else{
                sendNotification(PushNotification(NotificationData(userDataModel.firstName,"Sent for review!"),tokenId))
                mainViewModel.updateNotification(secondPersonDataModel, NotificationDataForUser(
                    Calendar.getInstance().timeInMillis.toString(),"Sent for review",it)
                )
            }

            Toast.makeText(requireContext(), "Done", Toast.LENGTH_SHORT).show()

            requireActivity().onBackPressed()

        })

        mainViewModel.userDataLive.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            tokenId = it.tokenId
            secondPersonDataModel = it
        })

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



    }

    private fun setData() {

        val data = Constants.loanData

        date = data.date

        binding.tvChanges.text = "Request : ${data.secondPersonComment} "

        if (userDataModel.phoneNumber == data.phone){
            userPhone = data.loanGiverPhone
        }else{
            userPhone = data.phone
        }

        mainViewModel.getUserData(userPhone)

        binding.edName.text = data.name

        binding.edAmount.setText(data.amount.toString())
        binding.edPhonenumber.text = data.phone
        if(!loanReason.contains(data.description)){
            binding.edDescription.setSelection(5)
            binding.edOthereasons.visibility = View.VISIBLE
            binding.edOthereasons.setText(data.description)
        }else{
            binding.edOthereasons.visibility = View.GONE
            binding.edDescription.setSelection(loanReason.indexOf(data.description))
        }

        binding.tvDateValue.text = getDate(data.date.toLong())

        if(data.endDate == "0L"){
            binding.tvEnddateValue.text = getString(R.string.selectdate)
        }else{
            binding.tvEnddateValue.text = getDate(data.endDate.toLong())
        }
        binding.edInterest.setText(data.interest.toString())
        binding.edEmi.setText(data.emi.toString())

        if(data.loanType.isNotEmpty()){
            binding.tvLoanvalue.text = data.loanType
            //   binding.edDescription.setSelection(loanType.indexOf(data.loanType))
        }

        if(data.paymentType == ADHOC){
            binding.tvPaymenttypevalue.text = ADHOC
            //   binding.edDescription.setSelection(loanType.indexOf(data.loanType))
        }else{
            binding.tvPaymenttypevalue.text = MONTHLY
        }

        if(data.status == Constants.PAID){
            binding.rbPaid.isChecked = true
        }else{
            binding.rbNotpaid.isChecked = true
        }
        binding.tvDateValue.text = getDate(date.toLong())



        val loanData = Constants.loanData

        if (userDataModel.userId != Constants.loanData.loanCreator){

            if (loanData.secondPersonComment.isEmpty()){
                binding.cdRequestforchange.visibility = View.VISIBLE
                binding.cdAccept.visibility = View.VISIBLE
                binding.tvOr.visibility = View.VISIBLE
                binding.edRequestchange.visibility = View.VISIBLE
                binding.ctViewloandetails.deepForEach { isEnabled = false }
                if (loanData.secondPersonStatus.isNotEmpty()){
                    binding.tvChanges.visibility = View.VISIBLE
                    binding.tvChanges.text = loanData.secondPersonStatus
                }
            }else{
                binding.tvChanges.visibility = View.VISIBLE
                binding.tvChanges.text = "Changes Request is in Review"
            }

        }else{
            if (loanData.secondPersonComment.isEmpty()){
                binding.tvChanges.visibility = View.VISIBLE
                binding.tvChanges.text = "Loan is in Review"
            }else{
                binding.cdMadechanges.visibility = View.VISIBLE
                binding.cdCantmakechanges.visibility = View.VISIBLE
                binding.tvChanges.visibility = View.VISIBLE
                binding.ctViewloandetails.deepForEach { isEnabled = true }
            }
        }


    }

    fun setSpinner(spinner: Spinner, spinnerList : List<String>, isReason : Boolean = false) {
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

            }
        }
    }

    override fun onStop() {
        super.onStop()
        Constants.isFromNotification = false
    }

}