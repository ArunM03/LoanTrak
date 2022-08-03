package com.loantrackingsystem.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.loantrackingsystem.app.ui.loan.EMICalendar
import com.loantrackingsystem.app.ui.loan.LoanTransactionsFragment
import com.loantrackingsystem.app.ui.loan.ViewLoanFragment


class LoanTabViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> ViewLoanFragment()
            1 -> EMICalendar()
            else -> LoanTransactionsFragment()
        }
    }

}