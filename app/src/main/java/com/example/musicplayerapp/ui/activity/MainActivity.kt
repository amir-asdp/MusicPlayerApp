package com.example.musicplayerapp.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.musicplayerapp.utils.PermissionUtils
import com.example.musicplayerapp.R
import com.example.musicplayerapp.databinding.ActivityMainBinding
import com.example.musicplayerapp.ui.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        PermissionUtils.requestMediaPermission(this){
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            binding.bottomNavigationView.setupWithNavController(navController)

            binding.bottomNavigationView.setOnItemSelectedListener {
                when(it.itemId){
                    R.id.navMenu_songs -> {
                        navController.navigate(R.id.navigation_songs)
                        true
                    }
                    else -> {
                        navController.navigate(R.id.navigation_play)
                        true
                    }
                }
            }
            navController.addOnDestinationChangedListener{ _, destination, _ ->
                when(destination.id){
                    R.id.navigation_songs -> {
                        binding.bottomNavigationView.menu.findItem(R.id.navMenu_songs).isChecked = true
                    }
                    R.id.navigation_play -> {
                        binding.bottomNavigationView.menu.findItem(R.id.navMenu_play).isChecked = true
                    }
                }
            }
        }
    }





}