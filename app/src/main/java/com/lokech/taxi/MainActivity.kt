package com.lokech.taxi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.lokech.taxi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout

        // setting up navigation UI
        val navController = findNavController(R.id.myNavHostFragment)
        setupActionBarNavigation(navController, drawerLayout)
        connectDrawerToController(binding.navView, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    // connect navigation drawer to navigation controller
    private fun connectDrawerToController(navView: NavigationView, navController: NavController) {
        NavigationUI.setupWithNavController(navView, navController)
    }

    // setup navigation on actionbar
    private fun setupActionBarNavigation(navController: NavController, drawerLayout: DrawerLayout) {
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
    }
}
