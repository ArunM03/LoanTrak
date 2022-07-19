package com.loantrackingsystem.app.ui.login

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.loantrackingsystem.adapter.ViewPageAdapter
import com.loantrackingsystem.app.R
import com.loantrackingsystem.app.data.UserData
import com.loantrackingsystem.app.databinding.FragmentLoginBinding
import com.loantrackingsystem.app.firebase.FirebaseViewmodel
import com.loantrackingsystem.app.other.Constants
import com.loantrackingsystem.app.other.MyDialog
import com.loantrackingsystem.app.other.SharedPref
import com.loantrackingsystem.app.room.MainViewModelFactory
import com.loantrackingsystem.app.room.MainViewmodel
import java.util.*


class LoginFragment : Fragment(R.layout.fragment_login) {


    lateinit var binding : FragmentLoginBinding
    lateinit var mainViewModel: FirebaseViewmodel
    var allUsers = listOf<UserData>()
    lateinit var sharedPref : SharedPref
    lateinit var myDialog: MyDialog
    var currentPage = 0
    var timer: Timer? = null
    val DELAY_MS: Long = 500 //delay in milliseconds before task is to be executed
    val PERIOD_MS: Long = 3000 // time in milliseconds between successive task executions.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginBinding.bind(view)



        sharedPref = SharedPref(requireContext())

/*        if(sharedPref.getUserLoginStatus()){
            Constants.userData = sharedPref.getUserData()
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                .navigate(R.id.action_loginFragment_to_pinFragment)
        }*/

            setUI(view)

    }

    private fun setUI(view: View) {

/*
        val mainViewModelFactory = MainViewModelFactory(requireActivity().application)
        mainViewModel =
            ViewModelProvider(this, mainViewModelFactory).get(MainViewmodel::class.java)
*/

        mainViewModel =
            ViewModelProvider(this).get(FirebaseViewmodel::class.java)

        myDialog = MyDialog(requireContext())

        mainViewModel.userLoginLive.observe(viewLifecycleOwner, Observer {

            myDialog.dismissProgressDialog()

            sharedPref.setUserDataModel(it)
            Constants.userDataModel = it

            if(Constants.isFirstTime){
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                    .navigate(R.id.action_loginFragment_to_nav_home)
            }else{
    /*            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                    .navigate(R.id.action_loginFragment_to_nav_gallery)*/
                            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                    .navigate(R.id.action_loginFragment_to_tabViewFragment)
            }
        })


        mainViewModel.errorUserLoginLive.observe(viewLifecycleOwner, Observer {

            myDialog.dismissProgressDialog()
            myDialog.showErrorAlertDialog(it)

        })

        binding.tvRegister.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
                .navigate(R.id.action_loginFragment_to_registerFragment)
        }



        binding.cdLogin.setOnClickListener {

            val userName = binding.edUsername.text.toString()
            val password = binding.edPassword.text.toString()

            if(userName.isNotEmpty() && password.isNotEmpty()){

              //  val users1 = allUsers.filter { it.username == userName }
            //    if(users1.isNotEmpty()){
                //    val users2 = users1.filter { it.password  == password}
                   // if(users2.isNotEmpty()){

                mainViewModel.loginUser(userName, password)

                myDialog.showProgressDialog("Loging In..Please wait",this)

             /*       }else {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.wrongpassword),
                            Toast.LENGTH_SHORT
                        ).show()
                    }*/
       /*         }else{
                    Toast.makeText(requireContext(), getString(R.string.wrongusername), Toast.LENGTH_SHORT).show()
                }*/

            }else{
                Toast.makeText(requireContext(), getString(R.string.pleaseenteralldetails), Toast.LENGTH_SHORT).show()
            }

        }


        val adapter = ViewPageAdapter()
        binding.viewpager.adapter = adapter

        val illusList = listOf(R.drawable.loanttak_cashimage,R.drawable.login_illustration,R.drawable.protection_illustration,R.drawable.protection_illustration)

        adapter.playlist = illusList


        val handler = Handler()
        val Update = Runnable {
            if (currentPage === adapter.itemCount - 1) {
                currentPage = 0
            }
            if (binding.viewpager != null){
                binding.viewpager.setCurrentItem(currentPage++, true)
            }
        }

        timer = Timer() // This will create a new Thread

        timer!!.schedule(object : TimerTask() {
            // task to be scheduled
            override fun run() {
                handler.post(Update)
            }
        }, DELAY_MS, PERIOD_MS)


        setViewPager()


    }

    fun setViewPager(){
       binding.viewpager.offscreenPageLimit = 1
        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            page.scaleY = 1 - (0.25f * Math.abs(position))
        }
        if(pageTransformer != null){
            binding.viewpager.setPageTransformer(pageTransformer)
        }
        val itemDecoration = HorizontalMarginItemDecoration(
            requireContext(),
            R.dimen.viewpager_current_item_horizontal_margin
        )
        binding.viewpager.addItemDecoration(itemDecoration)
    }
}
class HorizontalMarginItemDecoration(context: Context, @DimenRes horizontalMarginInDp: Int) :
    RecyclerView.ItemDecoration() {

    private val horizontalMarginInPx: Int =
        context.resources.getDimension(horizontalMarginInDp).toInt()

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.right = horizontalMarginInPx
        outRect.left = horizontalMarginInPx
    }

}