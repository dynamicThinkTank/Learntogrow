package com.androgeek.gymapplication.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.androgeek.gymapplication.MainActivity
import com.androgeek.gymapplication.R
import com.androgeek.gymapplication.databinding.ActivitySplashBinding
import android.graphics.drawable.Drawable
import com.androgeek.gymapplication.ui.utils.loadImage

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target


class SplashActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
       Glide.with(this).load(R.drawable.gym_guy1).into(binding.gifImage)
        nextActivity()

    }

    private fun nextActivity() {
        Handler().postDelayed({
            launchActivity()
        },2000)
    }

    private fun launchActivity() {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_splash)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}