package com.teikk.datn

import android.app.Application
import com.teikk.datn.data.model.Store
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    companion object {
        lateinit var store: Store
    }
}