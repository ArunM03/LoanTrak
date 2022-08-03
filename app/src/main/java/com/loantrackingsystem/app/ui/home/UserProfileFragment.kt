package com.loantrackingsystem.app.ui.home

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.loantrackingsystem.adapter.GroupAdapter
import com.loantrackingsystem.app.MainActivity
import com.loantrackingsystem.app.R
import com.loantrackingsystem.app.data.UserDataModel
import com.loantrackingsystem.app.databinding.FragmentProfileBinding
import com.loantrackingsystem.app.databinding.FragmentUserprofileBinding
import com.loantrackingsystem.app.firebase.FirebaseViewmodel
import com.loantrackingsystem.app.other.MyDialog
import com.loantrackingsystem.app.other.SharedPref

class UserProfileFragment : Fragment(R.layout.fragment_userprofile) {

    lateinit var binding : FragmentUserprofileBinding
    lateinit var adapter : GroupAdapter
    lateinit var mainViewModel: FirebaseViewmodel
    lateinit var sharedPref : SharedPref
    lateinit var edit : ImageButton
    var isEnabled = false
    lateinit var myDialog: MyDialog
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentUserprofileBinding.bind(view)

        setUI(view)

    }

    private fun setUI(view: View) {

        sharedPref = SharedPref(requireContext())

        mainViewModel =
            ViewModelProvider(this).get(
                FirebaseViewmodel::class.java)

        myDialog = MyDialog(requireContext())


       val userData = sharedPref.getUserDataModel()

        val activity = activity as MainActivity
        edit = activity.findViewById<ImageButton>(R.id.ib_edit)

        edit.visibility = View.VISIBLE

        setData(userData)

        edit.setOnClickListener {
            if(isEnabled){
                isEnabled = false
                binding.cdAddloan.visibility = View.GONE
                setNonEditableColor()
                edit.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.purple_500))
                binding.parentConstraint.deepForEach { isEnabled = false }
            }else   {
                binding.cdAddloan.visibility = View.VISIBLE
                isEnabled = true
                setEditableColor()
                edit.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue_200))
                binding.parentConstraint.deepForEach { isEnabled = true }
            }
        }


        binding.cdAddloan.setOnClickListener {

            val firstName = binding.edFirstname.text.toString()
            val lastName = binding.edLastname.text.toString()
            val phoneNumber = binding.edPhonenumber.text.toString()

            if(firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty()){
                Toast.makeText(requireContext(), "Please enter all details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mainViewModel.updateUser(firstName,lastName,phoneNumber,sharedPref.getUserDataModel().userId)

            myDialog.showProgressDialog("Updating...Please wait",this)

        }

        mainViewModel.userUpdatedLive.observe(viewLifecycleOwner, Observer {
            myDialog.dismissProgressDialog()
            sharedPref.setUserDataModel(sharedPref.getUserDataModel().apply {
                this.firstName = binding.edFirstname.text.toString()
                this.secondName = binding.edLastname.text.toString()
                this.phoneNumber = binding.edPhonenumber.text.toString()
            })
        })

        mainViewModel.errorUserUpdatedLive.observe(viewLifecycleOwner, Observer {
            myDialog.dismissProgressDialog()
            myDialog.showErrorAlertDialog(it)
        })



    }

    private fun setEditableColor() {
        binding.edFirstname.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.blue_100))
        binding.edLastname.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.blue_100))
        binding.edPhonenumber.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.blue_100))

    }

    private fun setNonEditableColor() { binding.edFirstname.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.grey_100))
        binding.edLastname.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.grey_100))
        binding.edPhonenumber.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.grey_100))
    }

    private fun setData(userData: UserDataModel) {

        binding.edFirstname.setText(userData.firstName)
        binding.edLastname.setText(userData.secondName)
        binding.edPhonenumber.setText(userData.phoneNumber)
        binding.edUsername.setText(userData.username)



    }
    fun ViewGroup.deepForEach(function: View.() -> Unit) {
        this.forEach { child ->
            child.function()
            if (child is ViewGroup) {
                child.deepForEach(function)
            }
        }
    }


}