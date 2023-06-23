package com.example.vetsoft.AMain

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.vetsoft.R

class Pcarga : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pcarga)
        Handler().postDelayed({
            startActivity(Intent(this@Pcarga, MainActivity::class.java))
            finish()
        }, 3000)
    }
}