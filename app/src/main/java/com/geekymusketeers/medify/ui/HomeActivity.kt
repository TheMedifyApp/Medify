package com.geekymusketeers.medify.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityHomeBinding
    private var timer = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        //Hides action bar
        supportActionBar?.hide()

        val bottomNavigationView = _binding.bottomNav
        val navController: NavController = findNavController(R.id.fragmentContainerView)
        AppBarConfiguration(setOf(R.id.home, R.id.stats, R.id.appointment, R.id.settings))

        bottomNavigationView.setupWithNavController(navController)

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

        val navController: NavController = findNavController(R.id.fragmentContainerView)
        val count = navController.backQueue.size

        if (count <= 2) {
            if (timer + 2000L > System.currentTimeMillis()) {
                finish()
//                onBackPressedDispatcher.onBackPressed()
            } else {
                Toast.makeText(
                    applicationContext, getString(R.string.press_once_again_to_exit),
                    Toast.LENGTH_SHORT
                ).show()
            }
            timer = System.currentTimeMillis()
        } else {
            navController.popBackStack()
        }
    }
}