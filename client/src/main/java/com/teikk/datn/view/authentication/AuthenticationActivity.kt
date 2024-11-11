package com.teikk.datn.view.authentication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.teikk.datn.R
import com.teikk.datn.base.BaseActivity
import com.teikk.datn.databinding.ActivityAuthenticationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationActivity : BaseActivity<ActivityAuthenticationBinding>() {
    private lateinit var navController: NavController
    override fun getLayoutResId(): Int {
        return R.layout.activity_authentication
    }

    override fun init() {
        navController = findNavController(R.id.loginNavController)
    }

    companion object {
        const val TAG = "AuthenticationActivity-TAG"
    }
}