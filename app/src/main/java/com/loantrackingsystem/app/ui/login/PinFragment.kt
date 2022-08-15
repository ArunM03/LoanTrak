package com.loantrackingsystem.app.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.loantrackingsystem.app.MainActivity
import com.loantrackingsystem.app.R
import com.loantrackingsystem.app.data.UserData
import com.loantrackingsystem.app.databinding.FragmentPinBinding
import com.loantrackingsystem.app.databinding.FragmentRegisterBinding
import com.loantrackingsystem.app.firebase.FirebaseViewmodel
import com.loantrackingsystem.app.other.Constants
import com.loantrackingsystem.app.other.MyDialog
import com.loantrackingsystem.app.other.SharedPref
import com.loantrackingsystem.app.room.MainViewModelFactory
import com.loantrackingsystem.app.room.MainViewmodel
import java.util.*


class PinFragment : Fragment(R.layout.fragment_pin) {

    lateinit var binding : FragmentPinBinding
    lateinit var sharedPref : SharedPref
    var isFirstTimePin = ""
    lateinit var mainViewModel: FirebaseViewmodel
    lateinit var myDialog : MyDialog
    var isPinDeleted = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPinBinding.bind(view)

        sharedPref = SharedPref(requireContext())

        setUI(view)
    }

    private fun setUI(view: View) {

/*        val mainViewModelFactory = MainViewModelFactory(requireActivity().application)
        mainViewModel =
            ViewModelProvider(this, mainViewModelFactory).get(MainViewmodel::class.java)*/

        mainViewModel =
            ViewModelProvider(this).get(
                FirebaseViewmodel::class.java)

        myDialog = MyDialog(requireContext())

        //val userData = sharedPref.getUserData()
        val userData = sharedPref.getUserDataModel()


        val illusList = listOf(R.drawable.languageillustration,R.drawable.login_illustration,R.drawable.protection_illustration)

        val curIllust = illusList[(0..2).random()]

        binding.ivLogin.setImageResource(curIllust)

        if(Constants.isSetPin){
            binding.tvForgetpassword.visibility = View.GONE
            binding.cdConfirmpin.visibility = View.VISIBLE
        }else{
            binding.tvForgetpassword.visibility = View.VISIBLE
            binding.cdConfirmpin.visibility = View.GONE
        }

        binding.tvForgetpassword.setOnClickListener {
            mainViewModel.updatePIN(userData.apply {
                this.pin = "null"
            })
            isPinDeleted = true
            myDialog.showProgressDialog("Please wait",this)
        }

        mainViewModel.errorPinCreatedLive.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            myDialog.dismissProgressDialog()
            myDialog.showErrorAlertDialog(it)

        })

        mainViewModel.pinCreatedLive.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            if (isPinDeleted){
                Constants.isLanguageChanged = false
                sharedPref.setUserData(UserData())
                sharedPref.setUserLoginStatus()
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
                Toast.makeText(requireContext(), "you will be logged out, login with user details and set mpin again. Your old mpin will be removed", Toast.LENGTH_LONG).show()

            }else{
                sharedPref.setUserDataModel(userData.apply {
                    this.pin = it.pin
                })

                sharedPref.setPin()

                Toast.makeText(requireContext(), "Pin Created", Toast.LENGTH_SHORT).show()

                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                    .navigate(R.id.action_pinFragment_to_nav_gallery)

                myDialog.dismissProgressDialog()
            }

        })

       // Toast.makeText(requireContext(), "$userData", Toast.LENGTH_SHORT).show()

        binding.cdLogin.setOnClickListener {

            val pin = binding.edPin.text.toString()
            val confirmPin = binding.edConfirmpin.text.toString()

            if(pin.isNotEmpty()){

                if(Constants.isSetPin){

                    if(pin.isEmpty() || confirmPin.isEmpty()){
                        Toast.makeText(requireContext(), "Please enter pin", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }else{

                        if (pin == confirmPin){

                            mainViewModel.updatePIN(userData.apply {
                                this.pin = pin
                            })

                     /*       mainViewModel.updateUser(userData.apply {
                                this.pin = pin
                            })*/

                            myDialog.showProgressDialog("Setting PIN...Please wait",this)

                            return@setOnClickListener
                        }else{
                            Toast.makeText(requireContext(), "Pin is not matching", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                    }

                }
                if(pin == userData.pin){

                    if (Constants.isPinChange) {
                        Constants.isSetPin = true
                        Constants.isPinChange = false
                        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                            .navigate(R.id.pinFragment)
                    }else{
                        Constants.isSetPin = false
                        Constants.isPinChange = false
                        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                            .navigate(R.id.action_pinFragment_to_tabViewFragment)
                    }
                /*        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                            .navigate(R.id.action_pinFragment_to_nav_gallery)*/


                }else{
                    Toast.makeText(requireContext(),getString(R.string.incorrectpin) + "${userData}", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(requireContext(),getString(R.string.enter_pinorpassword), Toast.LENGTH_SHORT).show()
            }

        }

    }

}