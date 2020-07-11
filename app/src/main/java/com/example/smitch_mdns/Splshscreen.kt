package com.example.smitch_mdns

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class Splshscreen : AppCompatActivity() {
    // Splash screen timer
    private val SPLASH_TIME_OUT = 3000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed(
                {
                    val i = Intent(this@Splshscreen, MainActivity::class.java)
                    startActivity(i)
                    finish()
                }, SPLASH_TIME_OUT)
    }
}