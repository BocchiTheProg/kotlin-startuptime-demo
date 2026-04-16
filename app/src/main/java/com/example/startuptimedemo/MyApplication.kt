package com.example.startuptimedemo

import android.app.Application
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApplication: Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    override fun onCreate(){
        super.onCreate()

        // Optimization: Moving heavy initialization off the Main Thread.
        // Allows the UI (MainActivity) to draw immediately while the SDK loads in the background.
        applicationScope.launch {
            simulateHeavyThirdPartyInit()
        }
    }

    private fun simulateHeavyThirdPartyInit() {
        Log.d("StartupDemo", "Starting heavy init...")
        // Simulating a 1.5-second delay (e.g., setting up a database or complex SDK)
        Thread.sleep(1500)
        Log.d("StartupDemo", "Heavy init complete!")
    }
}