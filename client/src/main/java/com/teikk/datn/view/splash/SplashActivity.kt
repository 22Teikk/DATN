package com.teikk.datn.view.splash

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil.setContentView
import com.teikk.datn.R
import com.teikk.datn.base.BaseActivity
import com.teikk.datn.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    private val viewModel: SplashViewModel by viewModels()
    override fun getLayoutResId(): Int {
        return R.layout.activity_splash
    }

    override fun init() {

    }
}