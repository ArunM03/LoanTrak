package com.loantrackingsystem.app.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.loantrackingsystem.app.R
import com.loantrackingsystem.app.databinding.FragmentPinBinding
import com.loantrackingsystem.app.databinding.FragmentSplashscreenBinding
import com.loantrackingsystem.app.other.Constants
import com.loantrackingsystem.app.other.SharedPref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenFragment : Fragment(R.layout.fragment_splashscreen) {

    lateinit var binding : FragmentSplashscreenBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSplashscreenBinding.bind(view)


        setUI(view)

    }

    private fun setUI(view: View) {




        CoroutineScope(Dispatchers.Main).launch {

            delay(2500)

            val sharedPref = SharedPref(requireContext())

            val curLang = sharedPref.getLanguage()
            if(Constants.isLanguageChanged){
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                    .navigate(R.id.action_splashScreenFragment_to_nav_gallery2)
            }else{
                if(curLang != "null"){
                    if (sharedPref.getUserLoginStatus() ){

                        if(sharedPref.getUserData().pin != "null"){
                            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                                .navigate(R.id.action_splashScreenFragment_to_pinFragment)
                        }else{
                            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                                .navigate(R.id.action_splashScreenFragment_to_loginFragment)
                        }
                    }else{
                        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                            .navigate(R.id.action_splashScreenFragment_to_loginFragment)
                    }
                }else{
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                        .navigate(R.id.action_splashScreenFragment_to_languageFragment)
                }
            }


        }
    }


}