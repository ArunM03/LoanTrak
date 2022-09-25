package com.loantrackingsystem.app.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.loantrackingsystem.app.MainActivity
import com.loantrackingsystem.app.R
import com.loantrackingsystem.app.databinding.FragmentCurrencyBinding
import com.loantrackingsystem.app.databinding.FragmentLanguageBinding
import com.loantrackingsystem.app.databinding.FragmentLoginBinding
import com.loantrackingsystem.app.databinding.FragmentPinBinding
import com.loantrackingsystem.app.other.Constants
import com.loantrackingsystem.app.other.SharedPref
import java.util.*

class CurrencyFragment : Fragment(R.layout.fragment_currency) {

    lateinit var binding : FragmentCurrencyBinding
    lateinit var sharedPref : SharedPref
    var language = "INR"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCurrencyBinding.bind(view)

        sharedPref = SharedPref(requireContext())

        setUI(view)

    }


    private fun setUI(view: View) {



        Constants.isFirstTime = true

     //   setSpinner(binding.spCurrency, listOf("INR","USD"))

        val sharedPref = SharedPref(requireContext())

        binding.rbEnglish.setOnCheckedChangeListener { compoundButton, b ->

            if(b){
                binding.tvSelctlanguage.text = "Select Language"
            }

        }

        binding.cdLanguages.setOnClickListener {
            showCurrencyDialog()
        }


        binding.rbTelugu.setOnCheckedChangeListener { compoundButton, b ->

            if(b){
                binding.tvSelctlanguage.text = "భాషను ఎంచుకోండి"
            }

        }

        binding.cdSelect.setOnClickListener {

            if(language == "INR"){
                sharedPref.setCurrency("INR")
            }else{
                sharedPref.setCurrency("USD")
            }

            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)

        }

    }

    fun showCurrencyDialog(){
        lateinit var dialog : androidx.appcompat.app.AlertDialog
        val customview =  layoutInflater.inflate(R.layout.dialog_currency,null,false)

        val inr =  customview.findViewById<MaterialCardView>(R.id.cd_inr)
        val usd =  customview.findViewById<MaterialCardView>(R.id.cd_usd)

        inr.setOnClickListener{
            binding.ivFlag.setImageResource(R.drawable.indianflag)
            binding.tvCurrency.text = "INR"
            language = "INR"
            dialog.dismiss()
        }


        usd.setOnClickListener{
            binding.ivFlag.setImageResource(R.drawable.usa)
            binding.tvCurrency.text = "USD"
            language = "USD"
            dialog.dismiss()
        }

        val builder = MaterialAlertDialogBuilder(requireContext())
            .setView(customview)


        dialog = builder.show()

    }



    fun setAppLocale(language : String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = requireActivity().resources.configuration
        config.setLocale(locale)
        requireContext().createConfigurationContext(config)
        requireContext().resources.updateConfiguration(config, requireActivity().resources.displayMetrics)
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }


/*    fun setSpinner(spinner: Spinner, spinnerList : List<String>) {
        val adapter = object :
            ArrayAdapter<Any>(
                requireContext(), R.layout.sp_layout,
                spinnerList
            ) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                return super.getDropDownView(position, convertView, parent).also {
                    if (position == spinner.selectedItemPosition) {
                        it.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.blue_100))
                    }
                }
            }
        }
        adapter.setDropDownViewResource(R.layout.sp_layout)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {



            }

            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                language = spinner.selectedItem.toString()

            }
        }
    }*/

}