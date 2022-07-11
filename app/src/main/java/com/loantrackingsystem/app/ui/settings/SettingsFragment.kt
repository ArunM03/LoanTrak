package com.loantrackingsystem.app.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.loantrackingsystem.app.R
import com.loantrackingsystem.app.databinding.FragmentAddloanBinding
import com.loantrackingsystem.app.databinding.FragmentSettingsBinding
import com.loantrackingsystem.app.room.MainViewmodel

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    lateinit var binding : FragmentSettingsBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSettingsBinding.bind(view)


        setUI(view)

    }

    private fun setUI(view: View) {

    }

}