package com.loantrackingsystem.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.loantrackingsystem.app.R
import com.loantrackingsystem.app.databinding.FragmentHomeBinding
import com.loantrackingsystem.app.databinding.FragmentLanguageBinding
import com.loantrackingsystem.app.other.SharedPref

class HomeFragment : Fragment(R.layout.fragment_home) {


    lateinit var binding : FragmentHomeBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)
        
        setUI(view)

    }

    private fun setUI(view: View) {


        binding.cdDashboard.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                .navigate(R.id.action_nav_home_to_nav_gallery)
        }

        binding.cdAddloan.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                .navigate(R.id.action_nav_home_to_addLoanFragment)

        }

    }

}