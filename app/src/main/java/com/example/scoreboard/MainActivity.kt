 package com.example.scoreboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.scoreboard.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

 @AndroidEntryPoint
 class MainActivity : AppCompatActivity() {

     private lateinit var binding :ActivityMainBinding
     private lateinit var navController :NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost : BottomNavigationView =binding.bottomNav
        navController =findNavController(R.id.nav_host)

        // Setup the ActionBar with navController and 4 top level destinations
        /**
         *

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.newMatchFragment,
                R.id.scoringFragment,
                R.id.matchListFragment,
                R.id.topPlayersFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
         */
        navHost.setupWithNavController(navController)

    }
     /**
      * Handle navigation when the user chooses Up from the action bar.
      */
     override fun onSupportNavigateUp(): Boolean {
         return navController.navigateUp() || super.onSupportNavigateUp()
     }

 }
