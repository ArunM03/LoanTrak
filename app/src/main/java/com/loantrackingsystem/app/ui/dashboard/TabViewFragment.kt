package com.loantrackingsystem.app.ui.dashboard

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayoutMediator
import com.loantrackingsystem.adapter.LoanHistoryAdapter
import com.loantrackingsystem.adapter.TabViewPagerAdapter
import com.loantrackingsystem.app.R
import com.loantrackingsystem.app.databinding.FragmentDashboardBinding
import com.loantrackingsystem.app.databinding.FragmentTabviewBinding
import com.loantrackingsystem.app.firebase.FirebaseViewmodel
import com.loantrackingsystem.app.other.Constants
import com.loantrackingsystem.app.other.SharedPref

class TabViewFragment : Fragment(R.layout.fragment_tabview) {


    lateinit var binding : FragmentTabviewBinding
    lateinit var mainViewModel: FirebaseViewmodel
    lateinit var sharedPref: SharedPref
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTabviewBinding.bind(view)

        setUI(view)

    }

    private fun setUI(view: View) {

        val adapter = TabViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        sharedPref = SharedPref(requireContext())

        mainViewModel =
            ViewModelProvider(this).get(
                FirebaseViewmodel::class.java)

        val userData = sharedPref.getUserDataModel()

       mainViewModel.getUserData(userData.phoneNumber)

        val count = sharedPref.getNotifications()

        if (count != 0){
            binding.cdNotificationcount.visibility = View.VISIBLE
            binding.tvNotificaitoncount.text = count.toString()
        }else{
            binding.cdNotificationcount.visibility = View.GONE
        }

        binding.tvDashboardTitle.text = "Hi, ${userData.firstName}"

        mainViewModel.userDataLive.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            sharedPref.setNotifciations(it.pendingNotificationCount)
            Constants.notificationList = it.notifications

            if (it.pendingNotificationCount != 0){
                binding.cdNotificationcount.visibility = View.VISIBLE
                binding.tvNotificaitoncount.text = sharedPref.getNotifications().toString()
            }else{
                binding.cdNotificationcount.visibility = View.GONE
            }
        })

        binding.ivNotification.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                .navigate(R.id.action_tabViewFragment_to_notificationFragment)
        }


        TabLayoutMediator(binding.tabLayout, binding.viewPager ) { tab, position ->
            tab.text = when (position){
                0 -> "Loan Given"
                else -> "Loan Taken"
            }
        }.attach()




    }

}