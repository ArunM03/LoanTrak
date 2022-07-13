package com.loantrackingsystem.app.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.loantrackingsystem.adapter.GroupAdapter
import com.loantrackingsystem.app.R
import com.loantrackingsystem.app.databinding.FragmentHomeBinding
import com.loantrackingsystem.app.databinding.FragmentProfileBinding
import com.loantrackingsystem.app.other.SharedPref
import com.loantrackingsystem.app.room.MainViewModelFactory
import com.loantrackingsystem.app.room.MainViewmodel

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    lateinit var binding : FragmentProfileBinding
    lateinit var adapter : GroupAdapter
    lateinit var mainViewModel: MainViewmodel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProfileBinding.bind(view)

        setUI(view)

    }

    private fun setUI(view: View) {

        val mainViewModelFactory = MainViewModelFactory(requireActivity().application)

        mainViewModel =
            ViewModelProvider(this, mainViewModelFactory).get(MainViewmodel::class.java)


        adapter = GroupAdapter()
        binding.rvPeople.adapter = adapter
        binding.rvPeople.layoutManager = LinearLayoutManager(requireContext())

        val sharedPref = SharedPref(requireContext())

        val userData = sharedPref.getUserData()

        mainViewModel.getAllLoanPerson(userData.username).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter.loanHistoryList = it
        })




    }

}