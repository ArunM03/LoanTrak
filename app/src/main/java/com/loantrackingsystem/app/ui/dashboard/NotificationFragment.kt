package com.loantrackingsystem.app.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.loantrackingsystem.adapter.NotificationAdapter
import com.loantrackingsystem.app.R
import com.loantrackingsystem.app.databinding.FragmentNotificationBinding
import com.loantrackingsystem.app.databinding.FragmentTabviewBinding
import com.loantrackingsystem.app.firebase.FirebaseViewmodel
import com.loantrackingsystem.app.other.Constants
import com.loantrackingsystem.app.other.SharedPref

class NotificationFragment : Fragment(R.layout.fragment_notification) {

    lateinit var binding : FragmentNotificationBinding
    lateinit var notificationAdapter: NotificationAdapter
    lateinit var mainViewModel: FirebaseViewmodel
    lateinit var sharedPref: SharedPref
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentNotificationBinding.bind(view)

        setUI(view)

    }

    private fun setUI(view: View) {

        notificationAdapter = NotificationAdapter()

        mainViewModel =
            ViewModelProvider(this).get(
                FirebaseViewmodel::class.java)

        sharedPref = SharedPref(requireContext())


        binding.rvNotifications.adapter = notificationAdapter
        binding.rvNotifications.layoutManager = LinearLayoutManager(requireContext())

        notificationAdapter.loanHistoryList = Constants.notificationList.sortedByDescending { it.time }

        notificationAdapter.setOnItemClickListener {
            if (it.loanId.isEmpty()){
                requireActivity().onBackPressed()
            }else{
                if (it.loanId == "Profile"){
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                        .navigate(R.id.action_notificationFragment_to_userProfileFragment)
                }else{
                    Constants.loanId = it.loanId
                    Constants.isFromNotification = true
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                        .navigate(R.id.action_notificationFragment_to_viewReviewLoanFragment)
                }
            }
        }

        mainViewModel.clearNotification(sharedPref.getUserDataModel())

        sharedPref.setNotifciations(0)


    }

}