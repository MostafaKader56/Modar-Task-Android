package com.modar.task.ui.routing

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.modar.task.ui.main.MainActivity

class RoutingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Keep the splash screen visible for this Activity.
        splashScreen.setKeepOnScreenCondition { true }

        openNextActivity()
    }

    private fun openNextActivity() {
        startActivity(Intent(this@RoutingActivity, MainActivity::class.java))
        finish()
    }
}