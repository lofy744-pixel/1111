package com.example

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

class NeovaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                try {
                    FirebaseApp.initializeApp(this)
                    Log.d("NeovaApplication", "FirebaseApp successfully initialized via default config")
                } catch (e: Exception) {
                    Log.w("NeovaApplication", "Default FirebaseApp initialization failed: ${e.message}. Using explicit FirebaseOptions fallback.")
                    val options = FirebaseOptions.Builder()
                        .setApiKey("AIzaSyCmOxg2Ud2Ceagwgo9jRrUhBkFbjf7hm6k")
                        .setApplicationId("1:957326634552:android:bb45737452cc3cb0fdc0cd")
                        .setProjectId("neova-store")
                        .setStorageBucket("neova-store.firebasestorage.app")
                        .setGcmSenderId("957326634552")
                        .build()
                    FirebaseApp.initializeApp(this, options)
                    Log.d("NeovaApplication", "FirebaseApp successfully initialized via explicit FirebaseOptions fallback")
                }
            } else {
                Log.d("NeovaApplication", "FirebaseApp already initialized")
            }
        } catch (t: Throwable) {
            Log.e("NeovaApplication", "Error initializing FirebaseApp: ${t.message}", t)
        }
    }
}
