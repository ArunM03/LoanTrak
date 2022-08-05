package com.loantrackingsystem.app.ui.loan

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.loantrackingsystem.adapter.EmiCalendarAdapter
import com.loantrackingsystem.adapter.LoanHistoryAdapter
import com.loantrackingsystem.app.MainActivity
import com.loantrackingsystem.app.R
import com.loantrackingsystem.app.data.*
import com.loantrackingsystem.app.databinding.FragmentLoanhistoryBinding
import com.loantrackingsystem.app.databinding.FragmentViewloanBinding
import com.loantrackingsystem.app.firebase.FirebaseViewmodel
import com.loantrackingsystem.app.other.Constants
import com.loantrackingsystem.app.other.MyDialog
import com.loantrackingsystem.app.room.MainViewModelFactory
import com.loantrackingsystem.app.room.MainViewmodel
import java.text.SimpleDateFormat
import java.util.*

class ViewLoanFragment : Fragment(R.layout.fragment_viewloan) {

    lateinit var binding : FragmentViewloanBinding
    lateinit var mainViewModel: FirebaseViewmodel
    var date = ""
    var number = ""
    lateinit var delete : ImageButton
    lateinit var edit : ImageButton
    var endDate = "0L"
    var isNew = false
    lateinit var emiCalendar: EmiCalendarAdapter
    lateinit var transactions: EmiCalendarAdapter
    lateinit var loanPersons : MutableList<String>
    lateinit var myDialog: MyDialog
    var isEnabled = false
    val loanReason = listOf("Car Loan","Hand Loan","Personal Loan","Education Loan","Home renovation Loan","Other")
    val loanType = listOf("Loan Given","Loan Taken")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentViewloanBinding.bind(view)

        setUI(view)

    }

    private fun setUI(view: View) {


  /*      val mainViewModelFactory = MainViewModelFactory(requireActivity().application)
        mainViewModel =
            ViewModelProvider(this, mainViewModelFactory).get(MainViewmodel::class.java)*/


        mainViewModel =
            ViewModelProvider(this).get(
                FirebaseViewmodel::class.java)

        myDialog = MyDialog(requireContext())

        binding.parentConstraint.deepForEach { isEnabled = false }

        setPaymentCalendar()
        setTransactions()

        loanPersons = mutableListOf<String>(getString(R.string.selectperson))

        binding.rvEmis.isNestedScrollingEnabled = false
        binding.rvTransactions.isNestedScrollingEnabled = false

        setSpinner(binding.edDescription, loanReason,true)
       // setSpinner(binding.edLoanType, loanType)

        val activity = activity as MainActivity
        delete = activity.findViewById<ImageButton>(R.id.ib_delete)
        edit = activity.findViewById<ImageButton>(R.id.ib_edit)
        delete.visibility = View.VISIBLE
        edit.visibility = View.VISIBLE

        delete.setOnClickListener {
            showAlertDialog()
        }

        edit.setOnClickListener {
            if(isEnabled){
                isEnabled = false
                setNonEditableColor()
                edit.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.purple_500))
                binding.parentConstraint.deepForEach { isEnabled = false }
            }else   {
                isEnabled = true
                setEditableColor()
                edit.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue_200))
                binding.parentConstraint.deepForEach { isEnabled = true }
            }
        }

     //   Toast.makeText(requireContext(), "${Constants.loanData}", Toast.LENGTH_SHORT).show()

        setData()

        binding.cdAddloan.setOnClickListener {

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


                mainViewModel.addLoan(LoanDataModel(name,phone, amount,reason,date,endDate,emi.toFloat(),interest.toFloat(), status,Constants.loanData.months,Constants.loanData.years,Constants.loanData.userid,loanType,Constants.loanData.username,Constants.loanData.loanGiverPhone,Constants.loanData.usersPhone, Constants.loanData.loanID))


                if(isNew){
               //     mainViewModel.addLoanPerson(LoanPersonData(name,phone,Constants.loanData.userid))
                }

                myDialog.showProgressDialog("Updating...Please wait",this)

            }else{
                Toast.makeText(requireContext(), getString(R.string.pleaseenteralldetails), Toast.LENGTH_SHORT).show()
            }

        }

        binding.edInterest.doAfterTextChanged {

            it?.let {

                if(it.isNotEmpty()){
                        val amount = binding.edAmount.text.toString()
                      if(amount.isNotEmpty()){
                        val emi = amount.toInt() * (it.toString().toFloat()/100)
                         binding.edEmi.setText(emi.toString())
                        }

                }

            }

        }

        binding.tvDateValue.setOnClickListener {

            showDatePickerDialog()
        }

        binding.cdEnddate.setOnClickListener {

            showDatePickerDialog(true)
        }

        binding.ibContact.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            startActivityForResult(intent, PICK_CONTACT)

        }

        mainViewModel.addLoanLive.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            myDialog.dismissProgressDialog()

            Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT)
                .show()

            requireActivity().onBackPressed()

        })

/*
        mainViewModel.getLoanPerson(Constants.loanData.userid)

        mainViewModel.getLoanPersonLive.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            loanPersons.addAll(getNames2(it))
        //    setSpinner(binding.edName,loanPersons)
            val index = loanPersons.indexOf(Constants.loanData.name)
            if(index >= 0){
                binding.edName.setSelection(index)
            }
        })
*/

        mainViewModel.errorAddLoanLive.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            myDialog.dismissProgressDialog()

            myDialog.showErrorAlertDialog(it)

        })

        mainViewModel.deleteLoanLive.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            myDialog.dismissProgressDialog()

            Toast.makeText(requireContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show()

            requireActivity().onBackPressed()

        })

        mainViewModel.errorDeleteLoanLive.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            myDialog.dismissProgressDialog()

            myDialog.showErrorAlertDialog(it)

        })


    }

    fun setPaymentCalendar(){

        emiCalendar = EmiCalendarAdapter()

        binding.rvEmis.adapter = emiCalendar
        binding.rvEmis.layoutManager = LinearLayoutManager(requireContext())


        emiCalendar.emiCalendarList = getEMICalendar()

        emiCalendar.setOnItemClickListener {
            Constants.curLoanEMIDate = it
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                .navigate(R.id.action_viewLoanFragment_to_updateTransactionFragment)
        }

    }

    fun setTransactions(){

        transactions = EmiCalendarAdapter()

        binding.rvTransactions.adapter = transactions
        binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext())

        transactions.emiCalendarList = Constants.loanData.emiData

        transactions.setOnItemClickListener {
            Constants.curLoanEMIDate = it
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                .navigate(R.id.action_viewLoanFragment_to_updateTransactionFragment)
        }

    }


    fun setEditableColor(){
        binding.edAmount.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.blue_100))
        binding.edInterest.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.blue_100))
        binding.tvDateValue.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.blue_100))
        binding.edEmi.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.blue_100))
    }
    fun setNonEditableColor(){
        binding.edAmount.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.grey_100))
        binding.edInterest.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.grey_100))
        binding.tvDateValue.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.grey_100))
        binding.edEmi.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.grey_100))
    }

    fun getEMICalendar() : List<LoanEmiData>{

        val emiCalendarList = mutableListOf<LoanEmiData>()

        val paidList = Constants.loanData.emiData.sortedBy { it.index }

        val calendar = Calendar.getInstance().apply {
            this.timeInMillis = Constants.loanData.date.toLong()
        }

        var month = calendar.get(Calendar.MONTH)
        var year = calendar.get(Calendar.YEAR)


        var emiAmount = Constants.loanData.emi

        val countDues = getNumberOfDues()

        for(emi in 1..countDues){

            month += 1

            if(month >= 13){
                month = 1
                year += 1
            }

            if(paidList.size >= emi){
                emiCalendarList.add(paidList[emi-1])
            }else{
                emiCalendarList.add(LoanEmiData(emi-1,"01/$month/$year",Constants.NOT_PAID,emiAmount.toString()))
            }

        }

        return emiCalendarList

    }

    private fun getNumberOfDues(): Int {

        val years = Constants.loanData.years.toInt()
        val months = Constants.loanData.months.toInt()

        val total = (years * 12 ) + months

        return total
    }

    fun getNames(loanPersonData: List<LoanPersonData>) : MutableList<String>{

        val names = mutableListOf<String>()
        for (person in loanPersonData){
            names.add(person.name)
        }
        return names
    }
    fun getNames2(loanPersonData: List<LoanPersonDataModel>) : MutableList<String>{

        val names = mutableListOf<String>()
        for (person in loanPersonData){
            names.add(person.name)
        }
        return names
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
    fun ViewGroup.deepForEach(function: View.() -> Unit) {
        this.forEach { child ->
            child.function()
            if (child is ViewGroup) {
                child.deepForEach(function)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        delete.visibility = View.INVISIBLE
        edit.visibility = View.INVISIBLE
    }

    private fun setData() {

        val data = Constants.loanData

        date = data.date

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



        if(data.status == Constants.PAID){
            binding.rbPaid.isChecked = true
        }else{
            binding.rbNotpaid.isChecked = true
        }
         binding.tvDateValue.text = getDate(date.toLong())

    }


    fun showDatePickerDialog(isEndDate : Boolean = false){

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.selectdate))
                .setSelection(date.toLong())
                .build()
        datePicker.addOnPositiveButtonClickListener {
            val yearFormat = SimpleDateFormat("MMM dd, yyyy")
            val timeText = yearFormat.format(it)
            if(isEndDate){
                binding.tvEnddateValue.text = timeText.toString()
                endDate = it.toString()
            }else{
                binding.tvDateValue.text = timeText.toString()
                date = it.toString()
            }
        }
        datePicker.show(childFragmentManager,"Match")

    }

/*    @SuppressLint("Range", "Recycle")
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

                    number = num
                    binding.edPhonenumber.setText(num)


                    // sendSMS(number)
                }

            }

        }

    }*/

    fun showAlertDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete))
            .setMessage(getString(R.string.areyousuredoyouwanttodeleteit))
            .setPositiveButton("Yes"){  _,_ ->
                mainViewModel.deleteLoan(Constants.loanData)
                myDialog.showProgressDialog("Deleting...Please wait",this)
            }
            .setNegativeButton("No",null)
            .show()
    }


}
fun Fragment.getDate(ms : Long) : String{
    val format = SimpleDateFormat("MMM dd, yyyy")
    return format.format(ms)
}