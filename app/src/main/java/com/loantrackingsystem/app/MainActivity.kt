package com.loantrackingsystem.app

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.MenuRes
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.loantrackingsystem.app.data.UserData
import com.loantrackingsystem.app.databinding.ActivityMainBinding
import com.loantrackingsystem.app.other.Constants
import com.loantrackingsystem.app.other.SharedPref
import java.io.File
import java.io.FileOutputStream
import java.util.*
import androidx.appcompat.view.menu.MenuBuilder

import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var sharedPref : SharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = SharedPref(this)
        val curLang = sharedPref.getLanguage().toString()
            if (curLang == "English") {
             setAppLocale("en")
         }else if(curLang == "తెలుగు") {
             setAppLocale("te")
         }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)





        setSupportActionBar(binding.appBarMain.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.tabViewFragment, R.id.loanHistoryFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        binding.appBarMain.layoutMain.bottomView.setupWithNavController(navController)





        binding.appBarMain.layoutMain.bottomView.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener{
            @SuppressLint("RestrictedApi")
            override fun onNavigationItemSelected(item: MenuItem): Boolean {

                if(item.itemId ==   R.id.profileFragment ){
                    Navigation.findNavController(this@MainActivity, R.id.nav_host_fragment_content_main)
                        .navigate(R.id.profileFragment)
                }
                if(item.itemId ==   R.id.tabViewFragment ){
                    Navigation.findNavController(this@MainActivity, R.id.nav_host_fragment_content_main)
                        .navigate(R.id.tabViewFragment)
                }
                if(item.itemId ==   R.id.loanHistoryFragment ){
                    Navigation.findNavController(this@MainActivity, R.id.nav_host_fragment_content_main)
                        .navigate(R.id.loanHistoryFragment)
                }
                if(item.itemId ==   R.id.addloan ){
                    Navigation.findNavController(this@MainActivity, R.id.nav_host_fragment_content_main)
                        .navigate(R.id.addLoanFragment)
                }
                if(item.itemId ==   R.id.nav_menu ){
                    showMenu(binding.appBarMain.layoutMain.tvMenu,R.menu.bottommenu)
                  /*  val popupMenu = PopupMenu(this@MainActivity,binding.appBarMain.layoutMain.tvMenu)
                    popupMenu.menuInflater.inflate(R.menu.bottommenu,popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener {
                        when(it.itemId){
                            R.id.about_us -> {
                                Navigation.findNavController(this@MainActivity, R.id.nav_host_fragment_content_main)
                                    .navigate(R.id.nav_home)
                                 true
                            }
                   *//*         R.id.set_pin -> {
                                Constants.isSetPin = true
                                Navigation.findNavController(this@MainActivity, R.id.nav_host_fragment_content_main)
                                    .navigate(R.id.pinFragment)
                                true
                            }*//*
                            R.id.action_settings -> {
                                Constants.isLanguageChanged = true
                                Navigation.findNavController(this@MainActivity, R.id.nav_host_fragment_content_main)
                                    .navigate(R.id.languageFragment)
                                true
                            }
                            R.id.action_inviteus -> {
                                share()
                                true
                            }
                            R.id.action_logout -> {
                                Constants.isLanguageChanged = false
                                sharedPref.setUserData(UserData())
                                sharedPref.setUserLoginStatus()
                                startActivity(Intent(this@MainActivity,MainActivity::class.java))
                                finish()
                                true
                            }
                            else ->{
                                true
                            }
                        }
                    }
                    val menuHelper = MenuPopupHelper(
                        this@MainActivity,
                        popupMenu.menu as MenuBuilder, binding.appBarMain.layoutMain.tvMenu
                    )
                    menuHelper.setForceShowIcon(true)
                    menuHelper.gravity = Gravity.END
                    popupMenu.show()*/

                }
                return true
            }


        })

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment,R.id.registerFragment ,R.id.pinFragment, R.id.languageFragment ,R.id.splashScreenFragment -> {
                    hideBottomNavigation()
                    hideToolbar()
                }
                R.id.nav_home, R.id.loanHistoryFragment,R.id.tabViewFragment,R.id.nav_gallery  -> {
                    hideToolbar()
                    showBottomNavigation()
                }
                else -> {
                    showToolbar()
                    hideBottomNavigation()
                }
            }
        }


    }

    fun setAppLocale(language : String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    @SuppressLint("RestrictedApi")
    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(this, v)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.about_us -> {
                    Navigation.findNavController(
                        this@MainActivity,
                        R.id.nav_host_fragment_content_main
                    )
                        .navigate(R.id.nav_home)
                    true
                }
                R.id.action_profilt -> {
                    Navigation.findNavController(
                        this@MainActivity,
                        R.id.nav_host_fragment_content_main
                    )
                        .navigate(R.id.userProfileFragment)
                    true
                }
        /*        R.id.set_pin -> {
                    Constants.isSetPin = true
                    Navigation.findNavController(
                        this@MainActivity,
                        R.id.nav_host_fragment_content_main
                    )
                        .navigate(R.id.pinFragment)
                    true
                }*/
                R.id.action_settings -> {
                    Constants.isLanguageChanged = true
                    Navigation.findNavController(
                        this@MainActivity,
                        R.id.nav_host_fragment_content_main
                    )
                        .navigate(R.id.languageFragment)
                    true
                }
                R.id.action_inviteus -> {
                    share()
                    true
                }
                R.id.action_logout -> {
                    Constants.isLanguageChanged = false
                    sharedPref.setUserData(UserData())
                    sharedPref.setUserLoginStatus()
                    startActivity(Intent(this@MainActivity, MainActivity::class.java))
                    finish()
                    true
                }
                else -> {
                    true
                }
            }
        }
        if (popup.menu is MenuBuilder) {
            val menuBuilder = popup.menu as MenuBuilder
            menuBuilder.setOptionalIconsVisible(true)
            for (item in menuBuilder.visibleItems) {
                val iconMarginPx =
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 6f, resources.displayMetrics)
                        .toInt()
                if (item.icon != null) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                        item.icon = InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx,0)
                    } else {
                        item.icon =
                            object : InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0) {
                                override fun getIntrinsicWidth(): Int {
                                    return intrinsicHeight + iconMarginPx + iconMarginPx
                                }
                            }
                    }
                }
            }
        }
        popup.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_settings -> {
                Constants.isLanguageChanged = true
                Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
                    .navigate(R.id.languageFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    fun hideToolbar(){
        binding.appBarMain.toolbar.visibility = View.GONE
    }

    fun showToolbar(){
        binding.appBarMain.toolbar.visibility = View.VISIBLE
    }

    private fun share() {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val text = "Hi,  Please check this LoanTrak App https://play.google.com/store/apps/details?id=" + packageName
            intent.putExtra(Intent.EXTRA_TEXT,text)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "share by"))
        }catch (e: Exception){
            Toast.makeText(this,"${e.message}", Toast.LENGTH_SHORT).show()
        }
    }


    fun hideBottomNavigation(){
        binding.appBarMain.layoutMain.bottomView.visibility = View.GONE
    }

    fun showBottomNavigation(){
        binding.appBarMain.layoutMain.bottomView.visibility = View.VISIBLE
    }

}