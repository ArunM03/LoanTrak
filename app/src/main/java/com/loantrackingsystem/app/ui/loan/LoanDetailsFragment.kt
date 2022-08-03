package com.loantrackingsystem.app.ui.loan

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.loantrackingsystem.adapter.LoanHistoryAdapter
import com.loantrackingsystem.adapter.LoanTabViewPagerAdapter
import com.loantrackingsystem.adapter.TabViewPagerAdapter
import com.loantrackingsystem.app.R
import com.loantrackingsystem.app.databinding.FragmentLoanhistoryBinding
import com.loantrackingsystem.app.databinding.FragmentLoantabviewBinding
import com.loantrackingsystem.app.firebase.FirebaseViewmodel
import com.loantrackingsystem.app.other.SharedPref

class LoanDetailsFragment : Fragment(R.layout.fragment_loantabview) {

    lateinit var binding : FragmentLoantabviewBinding
    lateinit var mainViewModel: FirebaseViewmodel
    lateinit var loanHistoryAdapter: LoanHistoryAdapter
    lateinit var sharedPref : SharedPref
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoantabviewBinding.bind(view)

        setUI(view)
    }

    private fun setUI(view: View) {


        val adapter = LoanTabViewPagerAdapter(this)
        binding.viewPager.adapter = adapter


        TabLayoutMediator(binding.tabLayout, binding.viewPager ) { tab, position ->
            tab.text = when (position){
                0 -> "Loan Details"
                1 -> "Payment calendar"
                else -> "Transactions"
            }
        }.attach()



    }


}