package com.loantrackingsystem.app.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.loantrackingsystem.adapter.LoanHistoryAdapter
import com.loantrackingsystem.adapter.TabViewPagerAdapter
import com.loantrackingsystem.app.R
import com.loantrackingsystem.app.databinding.FragmentDashboardBinding
import com.loantrackingsystem.app.databinding.FragmentTabviewBinding
import com.loantrackingsystem.app.firebase.FirebaseViewmodel
import com.loantrackingsystem.app.other.SharedPref

class TabViewFragment : Fragment(R.layout.fragment_tabview) {


    lateinit var binding : FragmentTabviewBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTabviewBinding.bind(view)

        setUI(view)

    }

    private fun setUI(view: View) {

        val adapter = TabViewPagerAdapter(this)
        binding.viewPager.adapter = adapter


        TabLayoutMediator(binding.tabLayout, binding.viewPager ) { tab, position ->
            tab.text = when (position){
                0 -> "Loan Given"
                else -> "Loan Taken"
            }
        }.attach()




    }

}