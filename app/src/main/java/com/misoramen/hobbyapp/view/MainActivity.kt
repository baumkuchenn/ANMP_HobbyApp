package com.misoramen.hobbyapp.view

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.misoramen.hobbyapp.R
import com.misoramen.hobbyapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        setSupportActionBar(binding.toolbar)
        navController = (supportFragmentManager.findFragmentById(R.id.hostFragment) as NavHostFragment).navController

        NavigationUI.setupWithNavController(binding.navView, navController)
        binding.bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.loginFragment || destination.id == R.id.registerFragment) {
                binding.bottomNav.visibility = View.GONE
                binding.toolbar.visibility = View.GONE
            } else {
                binding.bottomNav.visibility = View.VISIBLE
                binding.toolbar.visibility = View.VISIBLE
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(Gravity.LEFT)
        }
    }

}