package com.loantrackingsystem.app.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.loantrackingsystem.app.R
import com.loantrackingsystem.app.data.UserData
import com.loantrackingsystem.app.data.UserDataModel
import com.loantrackingsystem.app.databinding.FragmentLoginBinding
import com.loantrackingsystem.app.databinding.FragmentRegisterBinding
import com.loantrackingsystem.app.firebase.FirebaseViewmodel
import com.loantrackingsystem.app.other.Constants
import com.loantrackingsystem.app.other.MyDialog
import com.loantrackingsystem.app.other.SharedPref
import com.loantrackingsystem.app.room.MainViewModelFactory
import com.loantrackingsystem.app.room.MainViewmodel
import java.util.*


class RegisterFragment : Fragment(R.layout.fragment_register) {


    lateinit var binding : FragmentRegisterBinding
    lateinit var mainViewModel: FirebaseViewmodel
  //  var allUsers = listOf<UserData>()
    lateinit var sharedPref : SharedPref
    lateinit var myDialog : MyDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRegisterBinding.bind(view)

        sharedPref = SharedPref(requireContext())

        setUI(view)

    }

    private fun setUI(view: View) {

     /* val mainViewModelFactory = MainViewModelFactory(requireActivity().application)

        mainViewModel =
            ViewModelProvider(this, mainViewModelFactory).get(MainViewmodel::class.java)  */

        mainViewModel =
            ViewModelProvider(this).get(FirebaseViewmodel::class.java)

        myDialog = MyDialog(requireContext())

        mainViewModel.userCreatedLive.observe(viewLifecycleOwner, Observer {

            myDialog.dismissProgressDialog()

            sharedPref.setUserDataModel(it)

            Toast.makeText(requireContext(),getString(R.string.successfulregistered), Toast.LENGTH_SHORT).show()

            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                .navigate(R.id.action_registerFragment_to_loginFragment)

          //  allUsers = it

        })


        mainViewModel.errorUserCreatedLive.observe(viewLifecycleOwner, Observer {

            myDialog.dismissProgressDialog()
            myDialog.showErrorAlertDialog(it)

        })

        binding.tvLogin.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                .navigate(R.id.action_registerFragment_to_loginFragment)
        }


        binding.cdLogin.setOnClickListener {

            val userName = binding.edUsername.text.toString()
            val password = binding.edPassword.text.toString()
            val confirmPassword = binding.edConfirmpassword.text.toString()
            val pin = "null"//binding.edPin.text.toString()
            val ConfirmPin = "null"//binding.edConfirmpin.text.toString()

            if(userName.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){

               // val users1 = allUsers.filter { it.username == userName }
             //   if(users1.isEmpty()){

                    if(password == confirmPassword){

                      //  if(pin == ConfirmPin){

                        //    mainViewModel.addUser(UserData(userName,password,pin))

                        val userId = UUID.randomUUID().toString().substring(0,15)

                        mainViewModel.createUser(UserDataModel(userId,userName,password,pin))

                        myDialog.showProgressDialog("Registering...Please wait",this)

                   /*     }else{
                            Toast.makeText(requireContext(), getString(R.string.pinnotmatching), Toast.LENGTH_SHORT).show()

                        }*/

                    }else{

                        Toast.makeText(requireContext(), getString(R.string.passwordnotmatching), Toast.LENGTH_SHORT).show()

                    }

            /*    }else{
                    Toast.makeText(requireContext(), getString(R.string.usernamealreadytaken), Toast.LENGTH_SHORT).show()
                }
*/
            }else{
                Toast.makeText(requireContext(), getString(R.string.pleaseenteralldetails), Toast.LENGTH_SHORT).show()
            }

        }



    }

}
