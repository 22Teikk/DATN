package com.teikk.datn

import android.app.Application
import android.content.pm.PackageManager
import android.util.Log
import com.google.android.libraries.places.api.Places
import com.teikk.datn.data.model.Store
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val apiKey: String? = try {
            val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            appInfo.metaData?.getString("apiKey")
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }

        if (apiKey != null) {
            Log.d("API_KEY", "API Key: $apiKey")
        } else {
            Log.e("API_KEY", "API Key not found")
        }
        apiKey?.let { Places.initialize(this, it) }
    }
    companion object {
        lateinit var store: Store
    }
}