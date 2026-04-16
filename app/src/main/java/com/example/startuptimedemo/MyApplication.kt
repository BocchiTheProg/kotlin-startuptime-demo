package com.example.startuptimedemo

import android.app.Application
import android.util.Log

class MyApplication: Application() {
    override fun onCreate(){
        super.onCreate()

        // SIMULATION: Synchronous heavy initialization on the Main Thread
        simulateHeavyThirdPartyInit()
    }

    private fun simulateHeavyThirdPartyInit() {
        Log.d("StartupDemo", "Starting heavy init...")
        // Simulating a 1.5-second delay (e.g., setting up a database or complex SDK)
        Thread.sleep(1500)
        Log.d("StartupDemo", "Heavy init complete!")
    }
}