package com.loantrackingsystem.app.ui.loan

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.loantrackingsystem.adapter.LoanHistoryAdapter
import com.loantrackingsystem.app.MainActivity
import com.loantrackingsystem.app.R
import com.loantrackingsystem.app.data.LoanData
import com.loantrackingsystem.app.data.LoanDataModel
import com.loantrackingsystem.app.databinding.FragmentAddloanBinding
import com.loantrackingsystem.app.databinding.FragmentLoanhistoryBinding
import com.loantrackingsystem.app.firebase.FirebaseViewmodel
import com.loantrackingsystem.app.other.Constants
import com.loantrackingsystem.app.other.SharedPref
import com.loantrackingsystem.app.room.MainViewModelFactory
import com.loantrackingsystem.app.room.MainViewmodel

class LoanHistoryFragment : Fragment(R.layout.fragment_loanhistory) {

      lateinit var binding : FragmentLoanhistoryBinding
    lateinit var mainViewModel: FirebaseViewmodel
    lateinit var loanHistoryAdapter: LoanHistoryAdapter
    lateinit var sharedPref : SharedPref
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoanhistoryBinding.bind(view)

        setUI(view)
    }

    private fun setUI(view: View) {

/*
        val mainViewModelFactory = MainViewModelFactory(requireActivity().application)
        mainViewModel =
            ViewModelProvider(this, mainViewModelFactory).get(MainViewmodel::class.java)
*/

        mainViewModel =
            ViewModelProvider(this).get(
                FirebaseViewmodel::class.java)


        sharedPref = SharedPref(requireContext())

        loanHistoryAdapter = LoanHistoryAdapter()

        binding.rvLoanhistory.adapter = loanHistoryAdapter
        binding.rvLoanhistory.layoutManager = LinearLayoutManager(requireContext())

        loanHistoryAdapter.setOnItemClickListener {

            Constants.loanData = it
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                .navigate(R.id.action_loanHistoryFragment_to_viewLoanFragment)

        }

        mainViewModel.getLoans(sharedPref.getUserDataModel().userId)

        mainViewModel.getLoansLive.observe(viewLifecycleOwner, Observer {

            loanHistoryAdapter.loanHistoryList = it.filter { it.status == Constants.PAID } .sortedWith(compareBy(LoanDataModel::status, LoanDataModel::name))


            if(it.filter { it.status == Constants.PAID }.isNotEmpty()){
                binding.progressbar.visibility = View.INVISIBLE
                binding.rvLoanhistory.visibility = View.VISIBLE
                binding.ivNodatafound.visibility = View.INVISIBLE
                binding.tvNodatafound.visibility = View.INVISIBLE
            }else{
                binding.progressbar.visibility = View.VISIBLE
                binding.rvLoanhistory.visibility = View.VISIBLE
                binding.ivNodatafound.visibility = View.VISIBLE
                binding.tvNodatafound.visibility = View.VISIBLE
            }

        })

        mainViewModel.errorGetLoansLive.observe(viewLifecycleOwner, Observer {

            binding.progressbar.visibility = View.INVISIBLE
            Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()

        })

 /*       mainViewModel.getAllLoan(sharedPref.getUserData().username).observe(viewLifecycleOwner, Observer {

            loanHistoryAdapter.loanHistoryList = it.filter { it.status == Constants.PAID } .sortedWith(compareBy(LoanData::status, LoanData::name))

            if(it.isNotEmpty()){
                binding.progressbar.visibility = View.INVISIBLE
                binding.rvLoanhistory.visibility = View.VISIBLE
                binding.ivNodatafound.visibility = View.INVISIBLE
                binding.tvNodatafound.visibility = View.INVISIBLE
            }else{
                binding.progressbar.visibility = View.VISIBLE
                binding.rvLoanhistory.visibility = View.VISIBLE
                binding.ivNodatafound.visibility = View.VISIBLE
                binding.tvNodatafound.visibility = View.VISIBLE
            }

        })*/



    }

}