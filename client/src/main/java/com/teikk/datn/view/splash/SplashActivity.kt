package com.teikk.datn.view.splash

import android.content.Intent
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import com.teikk.datn.R
import com.teikk.datn.base.BaseActivity
import com.teikk.datn.databinding.ActivitySplashBinding
import com.teikk.datn.view.authentication.AuthenticationActivity
import com.teikk.datn.view.dashboard.DashBoardActivity
import com.teikk.datn.view.permission.PermissionActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    private val viewModel: SplashViewModel by viewModels()
    override fun getLayoutResId(): Int {
        return R.layout.activity_splash
    }

    override fun initObserve() {
        viewModel.isFirstTime.observe(this) {
            if (it) {
                val intent = Intent(this, PermissionActivity::class.java ).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
            } else {
                val intent = Intent(this, if (viewModel.uid == "") AuthenticationActivity::class.java else DashBoardActivity::class.java ).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                android.os.Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(intent)
                }, 2000)
            }
        }
    }

    companion object {
        private val TAG = "SplashActivity-TAG"
    }
}