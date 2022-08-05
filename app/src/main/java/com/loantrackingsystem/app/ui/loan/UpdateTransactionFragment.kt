package com.loantrackingsystem.app.ui.loan

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.loantrackingsystem.adapter.LoanHistoryAdapter
import com.loantrackingsystem.app.R
import com.loantrackingsystem.app.databinding.FragmentLoanhistoryBinding
import com.loantrackingsystem.app.databinding.FragmentUpdatetransactionBinding
import com.loantrackingsystem.app.firebase.FirebaseViewmodel
import com.loantrackingsystem.app.other.Constants
import com.loantrackingsystem.app.other.MyDialog
import com.loantrackingsystem.app.other.SharedPref

class UpdateTransactionFragment : Fragment(R.layout.fragment_updatetransaction) {


    lateinit var binding : FragmentUpdatetransactionBinding
    lateinit var mainViewModel: FirebaseViewmodel
    lateinit var sharedPref : SharedPref
    lateinit var myDialog : MyDialog
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentUpdatetransactionBinding.bind(view)

        setUI(view)

    }

    private fun setUI(view: View) {

        mainViewModel =
            ViewModelProvider(this).get(
                FirebaseViewmodel::class.java)

        myDialog = MyDialog(requireContext())


        setData()


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

    private fun setData() {

        var data = Constants.curLoanEMIDate

        binding.edAmount.setText(data.amount)
        binding.tvDateValue.text = data.date

        if (data.status == Constants.PAID){
            binding.rbPaid.isChecked = true
        }else{
            binding.rbNotpaid.isChecked = true
        }

        binding.cdAddloan.setOnClickListener {

            var loanData = Constants.loanData

            val emiList = loanData.emiData.toMutableList()

            if(loanData.emiData.filter {it.index == data.index }.isEmpty()){

                data.amount = binding.edAmount.text.toString()
                data.status = if(binding.rbNotpaid.isChecked) Constants.NOT_PAID else Constants.PAID


                emiList.add(data)
                Constants.loanData.apply {
                    this.emiData = emiList
                }
                mainViewModel.addLoan(Constants.loanData)


                myDialog.showProgressDialog("Updating...Please wait",this)

            }else{

                Constants.loanData.emiData.filter {it.index == data.index }[0].apply {
                    this.amount = binding.edAmount.text.toString()
                    this.status = if(binding.rbNotpaid.isChecked) Constants.NOT_PAID else Constants.PAID
                }

                mainViewModel.addLoan(Constants.loanData)

                myDialog.showProgressDialog("Updating...Please wait",this)

            }
        }



    }



}