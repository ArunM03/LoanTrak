package com.loantrackingsystem.app.ui.loan

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.loantrackingsystem.adapter.EmiCalendarAdapter
import com.loantrackingsystem.app.R
import com.loantrackingsystem.app.data.LoanEmiData
import com.loantrackingsystem.app.databinding.FragmentEmicalendarBinding
import com.loantrackingsystem.app.databinding.FragmentTabviewBinding
import com.loantrackingsystem.app.other.Constants
import java.util.*

class EMICalendar  : Fragment(R.layout.fragment_emicalendar) {

    lateinit var binding : FragmentEmicalendarBinding
    lateinit var emiCalendar: EmiCalendarAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentEmicalendarBinding.bind(view)

        setUI(view)

    }

    private fun setUI(view: View) {

        emiCalendar = EmiCalendarAdapter()

        binding.rvEmis.adapter = emiCalendar
        binding.rvEmis.layoutManager = LinearLayoutManager(requireContext())


        emiCalendar.emiCalendarList = getEMICalendar()

        emiCalendar.setOnItemClickListener {
            Constants.curLoanEMIDate = it
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                .navigate(R.id.action_loanDetailsFragment_to_updateTransactionFragment)
        }


    }

    fun getEMICalendar() : List<LoanEmiData>{

        val emiCalendarList = mutableListOf<LoanEmiData>()

        val paidList = Constants.loanData.emiData.sortedBy { it.index }

        val calendar = Calendar.getInstance().apply {
            this.timeInMillis = Constants.loanData.date.toLong()
        }

        var month = calendar.get(Calendar.MONTH)
        var year = calendar.get(Calendar.YEAR)


        var emiAmount = Constants.loanData.emi

        val countDues = getNumberOfDues()

        for(emi in 1..countDues){

            month += 1

            if(month >= 13){
                month = 1
                year += 1
            }

            if(paidList.size >= emi){
                emiCalendarList.add(paidList[emi-1])
            }else{
                emiCalendarList.add(LoanEmiData(emi-1,"01/$month/$year",Constants.NOT_PAID,emiAmount.toString()))
            }

        }

        return emiCalendarList

    }

    private fun getNumberOfDues(): Int {

        val years = Constants.loanData.years.toInt()
        val months = Constants.loanData.months.toInt()

        val total = (years * 12 ) + months

        return total
    }


}