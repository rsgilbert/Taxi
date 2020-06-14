package com.lokech.taxi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.navigation.NavigationView
import com.lokech.taxi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout

    // appBarConfiguration contains top level destinations
    lateinit var appBarConfiguration: AppBarConfiguration

    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)
        drawerLayout = binding.drawerLayout
        appBarConfiguration =
            AppBarConfiguration(setOf(R.id.journeyListFragment), drawerLayout)
        navController = findNavController(R.id.myNavHostFragment)


        setupActionBar()
        connectDrawerToController(binding.navView)
    }


    // Allow navigation to previous fragments using up arrow in actionbar
    // AppbarConfiguration provides top level destinations
    override fun onSupportNavigateUp() =
        navController.navigateUp(appBarConfiguration)

}

// Have NavigationUI decide what label to show in the action bar
// It will also determine whether to show up arrow or drawer menu icon
private fun MainActivity.setupActionBar() =
    setupActionBarWithNavController(navController, appBarConfiguration)

// connect navigation drawer to navigation controller
private fun MainActivity.connectDrawerToController(navView: NavigationView) =
    NavigationUI.setupWithNavController(navView, navController)
