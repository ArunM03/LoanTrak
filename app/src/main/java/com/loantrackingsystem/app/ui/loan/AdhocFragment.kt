package com.loantrackingsystem.app.ui.loan

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.loantrackingsystem.app.R
import com.loantrackingsystem.app.data.AdhocEmiData
import com.loantrackingsystem.app.databinding.FragmentAddloanBinding
import com.loantrackingsystem.app.databinding.FragmentAdhocBinding
import com.loantrackingsystem.app.firebase.FirebaseViewmodel
import com.loantrackingsystem.app.other.Constants
import com.loantrackingsystem.app.other.MyDialog
import java.text.SimpleDateFormat
import java.util.*

class AdhocFragment : Fragment(R.layout.fragment_adhoc) {

    lateinit var binding : FragmentAdhocBinding
    var date = ""
    var lastAdhocEmiData = AdhocEmiData()
    lateinit var mainViewModel: FirebaseViewmodel
    lateinit var myDialog : MyDialog
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAdhocBinding.bind(view)

        setUI()

    }

    private fun setUI() {

        myDialog = MyDialog(requireContext())

        mainViewModel =
            ViewModelProvider(this).get(
                FirebaseViewmodel::class.java)

        date = Calendar.getInstance().timeInMillis.toString()

        setData()

    }

    @SuppressLint("SetTextI18n")
    private fun setData() {

        val data = Constants.loanData

        val durationDays = ((Calendar.getInstance().timeInMillis - Constants.loanData.date.toLong()) / (1000 * 60 * 60 * 24))
        binding.tvNumofdaysValue.text = durationDays.toString()
        if (data.adhocEmiDataList.isNotEmpty()){
            val adhocEmiList = data.adhocEmiDataList.sortedBy { it.date.toLong() }
            val remainingAmount = adhocEmiList[adhocEmiList.size-1]
            lastAdhocEmiData = remainingAmount
            binding.tvPrincipalamountValue.text = remainingAmount.remainingPrincipleAmount
            val remainingDurationDays = ((date.toLong() - lastAdhocEmiData.date.toLong()) / (1000 * 60 * 60 * 24))
            val interestAmount = ((lastAdhocEmiData.remainingPrincipleAmount.toLong() * remainingDurationDays * data.interest * 12  ) / 36500) + lastAdhocEmiData.remainingInterestAmount.toLong()
            binding.tvInterestamountValue.text = interestAmount.toInt().toString()
            binding.tvFinalamountValue.text = (interestAmount + lastAdhocEmiData.remainingPrincipleAmount.toLong()).toInt().toString()
        }else{
            binding.tvPrincipalamountValue.text = data.amount
            val interestAmount = (data.amount.toLong() * durationDays * data.interest * 12  ) / 36500
            binding.tvInterestamountValue.text = interestAmount.toInt().toString()
            binding.tvFinalamountValue.text = (interestAmount + data.amount.toLong()).toInt().toString()
        }

        binding.edAmount.text = data.interest.toString()
        binding.tvDateValue.text = getDate(data.date.toLong())
        binding.tvEnddateValue.text = getDate(Calendar.getInstance().timeInMillis)


        binding.tvEnddateValue.setOnClickListener {
            showDatePickerDialog()
        }

        binding.cdAddloan.setOnClickListener {
            updateLoanData()
        }

        mainViewModel.addLoanLive.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            myDialog.dismissProgressDialog()

            Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT)
                .show()

            requireActivity().onBackPressed()

        })

        mainViewModel.errorAddLoanLive.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            myDialog.dismissProgressDialog()

            myDialog.showErrorAlertDialog(it)

        })


    }

    private fun updateLoanData() {
        try {
            if (lastAdhocEmiData.paidInterestAmount.isEmpty()){
                val paidPrincipleAmount = binding.tvPrincipleRepaymentValue.text.toString()
                val paidInterestAmount = binding.tvInterestRepaymentValue.text.toString()
                val remainingPrincipleAmount = binding.tvPrincipalamountValue.text.toString().toLong() - paidPrincipleAmount.toLong()
                val remainingInterestAmount = binding.tvInterestamountValue.text.toString().toLong()  - paidInterestAmount.toLong()
                val adhocEmiData = AdhocEmiData(0,date,paidPrincipleAmount,paidInterestAmount, remainingPrincipleAmount.toString(),remainingInterestAmount.toString())

                Constants.loanData.apply {
                    this.adhocEmiDataList = listOf(adhocEmiData)
                }

            }else{
                val paidPrincipleAmount = binding.tvPrincipleRepaymentValue.text.toString()
                val paidInterestAmount = binding.tvInterestRepaymentValue.text.toString()
                val remainingPrincipleAmount =  binding.tvPrincipalamountValue.text.toString().toLong()  - paidPrincipleAmount.toLong()
                val remainingInterestAmount = binding.tvInterestamountValue.text.toString().toLong() - paidInterestAmount.toLong()
                val adhocEmiData = AdhocEmiData(lastAdhocEmiData.index++,date,paidPrincipleAmount,paidInterestAmount, remainingPrincipleAmount.toString(),remainingInterestAmount.toString())
                val adhocList = Constants.loanData.adhocEmiDataList.toMutableList()
                adhocList.add(adhocEmiData)
                Constants.loanData.apply {
                    this.adhocEmiDataList = adhocList
                }

            }

            mainViewModel.addLoan(Constants.loanData)
            myDialog.showProgressDialog("Updating...Please wait",this)


        }catch (e: Exception){
            myDialog.showErrorAlertDialog(e.message)
        }

    }

    @SuppressLint("SetTextI18n")
    fun showDatePickerDialog(){

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.selectdate))
                .setSelection(Calendar.getInstance().timeInMillis)
                .build()
        datePicker.addOnPositiveButtonClickListener {
            val yearFormat = SimpleDateFormat("MMM dd, yyyy")
            val timeText = yearFormat.format(it)
            binding.tvEnddateValue.text = timeText.toString()
            date = it.toString()
            val durationDays = ((it - Constants.loanData.date.toLong()) / (1000 * 60 * 60 * 24))
            binding.tvNumofdaysValue.text = durationDays.toString()
            if (lastAdhocEmiData.paidInterestAmount.isNotEmpty()){
                val remainingDurationDays = ((it - lastAdhocEmiData.date.toLong()) / (1000 * 60 * 60 * 24))
                val data = Constants.loanData
                val interestAmount = ((lastAdhocEmiData.remainingPrincipleAmount.toLong() * remainingDurationDays * data.interest * 12  ) / 36500) + lastAdhocEmiData.remainingInterestAmount.toLong()
                binding.tvInterestamountValue.text = interestAmount.toInt().toString()
                binding.tvFinalamountValue.text = (interestAmount + lastAdhocEmiData.remainingPrincipleAmount.toLong()).toInt().toString()
            }else{
                val data = Constants.loanData
                val interestAmount = (data.amount.toLong() * durationDays * data.interest * 12  ) / 36500
                binding.tvInterestamountValue.text = interestAmount.toInt().toString()
                binding.tvFinalamountValue.text = (interestAmount + data.amount.toLong()).toInt().toString()
            }

        }

        datePicker.show(childFragmentManager,"Match")

    }


}