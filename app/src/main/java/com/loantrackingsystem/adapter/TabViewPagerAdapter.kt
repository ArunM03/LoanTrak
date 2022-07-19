package com.loantrackingsystem.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.loantrackingsystem.app.ui.dashboard.DashboardFragment


class TabViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> DashboardFragment("Loan Given")
            else -> DashboardFragment("Loan Taken")
        }
    }

}