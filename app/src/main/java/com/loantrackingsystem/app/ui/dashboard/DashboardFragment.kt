package com.loantrackingsystem.app.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.provider.SyncStateContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.loantrackingsystem.app.databinding.FragmentDashboardBinding
import com.loantrackingsystem.app.room.MainViewmodel
import com.github.mikephil.charting.components.Legend

import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.formatter.PercentFormatter

import com.github.mikephil.charting.data.PieData

import com.github.mikephil.charting.utils.ColorTemplate

import com.github.mikephil.charting.utils.MPPointF

import com.github.mikephil.charting.data.PieDataSet


import com.github.mikephil.charting.data.PieEntry
import com.loantrackingsystem.adapter.LoanHistoryAdapter
import com.loantrackingsystem.app.R
import com.loantrackingsystem.app.data.LoanData
import com.loantrackingsystem.app.data.LoanDataModel
import com.loantrackingsystem.app.firebase.FirebaseViewmodel
import com.loantrackingsystem.app.other.Constants
import com.loantrackingsystem.app.other.SharedPref
import com.loantrackingsystem.app.room.MainViewModelFactory
import okhttp3.internal.notifyAll


class DashboardFragment(val type : String = "Loan Given") : Fragment(R.layout.fragment_dashboard) {


    lateinit var binding : FragmentDashboardBinding
    lateinit var mainViewModel: FirebaseViewmodel
    lateinit var loanHistoryAdapter: LoanHistoryAdapter
    lateinit var underReviewLoansAdapter : LoanHistoryAdapter
    lateinit var sharedPref: SharedPref
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDashboardBinding.bind(view)

        setUI(view)

    }

    private fun setUI(view: View) {

    /*    val mainViewModelFactory = MainViewModelFactory(requireActivity().application)
        mainViewModel =
            ViewModelProvider(this, mainViewModelFactory).get(MainViewmodel::class.java)*/

        mainViewModel =
            ViewModelProvider(this).get(
                FirebaseViewmodel::class.java)

        sharedPref = SharedPref(requireContext())

        val userData = sharedPref.getUserDataModel()

        loanHistoryAdapter = LoanHistoryAdapter(true,userData.phoneNumber)
        underReviewLoansAdapter = LoanHistoryAdapter(true,userData.phoneNumber)

        binding.cdAddloan.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                .navigate(R.id.action_tabViewFragment_to_addLoanFragment)

        }

        binding.switchHideshow.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                binding.tvTotalloanamount.visibility = View.VISIBLE
            }else{
                binding.tvTotalloanamount.visibility = View.GONE
            }
        }

        binding.rvLoans.adapter = loanHistoryAdapter
        binding.rvLoans.layoutManager = LinearLayoutManager(requireContext())

        binding.rvUnderreviewloans.adapter = underReviewLoansAdapter
        binding.rvUnderreviewloans.layoutManager = LinearLayoutManager(requireContext())

        mainViewModel.getLoans(userData.userId,type,userData.phoneNumber)

        mainViewModel.getLoansLive.observe(viewLifecycleOwner, Observer {

        //    Toast.makeText(requireContext(), "${it.size}", Toast.LENGTH_SHORT).show()

            binding.progressbar.visibility = View.INVISIBLE

            val pendingLoans = it.filter { it.status == Constants.NOT_PAID }.filter { it.isInReview != Constants.YES }.sortedBy { it.name }

            if(it.isNotEmpty()){
                binding.rvLoans.visibility = View.VISIBLE
                binding.ivNodatafound.visibility = View.INVISIBLE
                binding.tvNodatafound.visibility = View.INVISIBLE
                binding.scrollviewContent.visibility = View.VISIBLE
            }else{
                binding.rvLoans.visibility = View.INVISIBLE
                binding.ivNodatafound.visibility = View.VISIBLE
                binding.tvNodatafound.visibility = View.VISIBLE
                binding.scrollviewContent.visibility = View.INVISIBLE
            }

            if(pendingLoans.isEmpty()){
                binding.piechart.visibility = View.GONE
                binding.tvPendingloans.visibility = View.GONE
                binding.rvLoans.visibility = View.GONE
            }else{
                binding.piechart.visibility = View.VISIBLE
                binding.tvPendingloans.visibility = View.VISIBLE
                binding.rvLoans.visibility = View.VISIBLE
            }

            binding.tvTotalunpaidloan.text = pendingLoans.size.toString()
            binding.tvTotalloan.text = it.size.toString()

            binding.tvTotalloanamount.text = "₹${getTotalAmount(pendingLoans)}"

            loanHistoryAdapter.loanHistoryList = pendingLoans

            setData(pendingLoans.take(5))

            val loansUnderReview = it.filter { it.status == Constants.NOT_PAID }.filter { it.isInReview == Constants.YES }.sortedBy { it.name }

            if (loansUnderReview.isEmpty()){
                binding.tvReviewloans.visibility = View.GONE
                binding.rvUnderreviewloans.visibility = View.GONE
                binding.ctLoansreviewTitle.visibility = View.GONE
            }else{
                binding.tvReviewloans.visibility = View.VISIBLE
                binding.rvUnderreviewloans.visibility = View.VISIBLE
                binding.ctLoansreviewTitle.visibility = View.VISIBLE
            }

            underReviewLoansAdapter.loanHistoryList = loansUnderReview

        })

        mainViewModel.errorGetLoansLive.observe(viewLifecycleOwner, Observer {

            binding.progressbar.visibility = View.INVISIBLE
            Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()

        })

    /*    mainViewModel.getAllLoan(sharedPref.getUserData().username).observe(viewLifecycleOwner, Observer {

            binding.progressbar.visibility = View.INVISIBLE

            val pendingLoans = it.filter { it.status == Constants.NOT_PAID }.sortedBy { it.name }

            if(it.isNotEmpty()){
                binding.rvLoans.visibility = View.VISIBLE
                binding.ivNodatafound.visibility = View.INVISIBLE
                binding.tvNodatafound.visibility = View.INVISIBLE
                binding.scrollviewContent.visibility = View.VISIBLE
            }else{
                binding.rvLoans.visibility = View.INVISIBLE
                binding.ivNodatafound.visibility = View.VISIBLE
                binding.tvNodatafound.visibility = View.VISIBLE
                binding.scrollviewContent.visibility = View.INVISIBLE
            }

            if(pendingLoans.isEmpty()){
                binding.piechart.visibility = View.GONE
                binding.tvPendingloans.visibility = View.GONE
                binding.rvLoans.visibility = View.GONE
            }else{
                binding.piechart.visibility = View.VISIBLE
                binding.tvPendingloans.visibility = View.VISIBLE
                binding.rvLoans.visibility = View.VISIBLE
            }

            binding.tvTotalunpaidloan.text = pendingLoans.size.toString()
            binding.tvTotalloan.text = it.size.toString()

            binding.tvTotalloanamount.text = "₹${getTotalAmount(pendingLoans)}"

            loanHistoryAdapter.loanHistoryList = pendingLoans

            setData(pendingLoans)



        })*/

        loanHistoryAdapter.setOnItemClickListener {
            Constants.loanData = it
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                .navigate(R.id.action_tabViewFragment_to_viewLoanFragment)
        }

        underReviewLoansAdapter.setOnItemClickListener {
            Constants.loanData = it
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                .navigate(R.id.action_tabViewFragment_to_viewReviewLoanFragment)
        }


        setPieChart()


    }

    fun getTotalAmount(it : List<LoanDataModel>) : Long{

        var amount = 0L

        for(i in it){
            amount += i.amount.toLong()
        }

        return amount
    }



    fun setPieChart(){

        binding.piechart.setUsePercentValues(true)
        binding.piechart.description.isEnabled = false
        binding.piechart.setExtraOffsets(5f, 10f, 5f, 5f)

        binding.piechart.dragDecelerationFrictionCoef = 0.95f

/*
        binding.piechart.setCenterTextTypeface(tfLight);
        binding.piechart.centerText = generateCenterSpannableText();*/

        binding.piechart.isDrawHoleEnabled = true
        binding.piechart.setHoleColor(Color.WHITE)


        binding.piechart.setTransparentCircleColor(Color.WHITE)
        binding.piechart.setTransparentCircleAlpha(110)

      //  binding.piechart.holeRadius = 58f
        binding.piechart.transparentCircleRadius = 61f;

        binding.piechart.setDrawCenterText(true)

        binding.piechart.rotationAngle = 0F
        // enable rotation of the binding.piechart by touch
        binding.piechart.isRotationEnabled = true
        binding.piechart.isHighlightPerTapEnabled = true

        // add a selection listener
        binding.piechart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener{
            override fun onValueSelected(e: Entry?, h: Highlight?) {

            }

            override fun onNothingSelected() {

            }

        })

        binding.piechart.animateY(1400, Easing.EaseInOutQuad)
        // chart.spin(2000, 0, 360);

        // chart.spin(2000, 0, 360);
        val l: Legend = binding.piechart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f

        // entry label styling

        // entry label styling
        binding.piechart.setEntryLabelColor(Color.WHITE)
    //    binding.piechart.setEntryLabelTypeface(tfRegular)
        binding.piechart.setEntryLabelTextSize(12f)
        binding.piechart.setDrawEntryLabels(false)

        val legend: Legend? =  binding.piechart.legend
        legend?.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT

    }

    private fun setData(list : List<LoanDataModel>) {

        val entries: ArrayList<PieEntry> = ArrayList()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (loan in list) {
            entries.add(
                PieEntry(
                    loan.amount.toFloat(),
                    loan.name
                )
            )
        }
        val dataSet = PieDataSet(entries, getString(R.string.all_pending_loans))
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0F, 40F)
        dataSet.selectionShift = 5f

       // dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE)

        // add a lot of colors
        val colors: ArrayList<Int> = ArrayList()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.BLACK)
      //  data.setValueTypeface(tfLight)
        binding.piechart.setData(data)



        // undo all highlights
        binding.piechart.highlightValues(null)
        binding.piechart.invalidate()
    }


}