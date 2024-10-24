package com.teikk.datn.view.authentication

import android.util.Log
import androidx.activity.viewModels
import com.teikk.datn.R
import com.teikk.datn.base.BaseActivity
import com.teikk.datn.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel: AuthenticationViewModel by viewModels()
    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun initEvent() {
        binding.apply {
            button.setOnClickListener {
                viewModel.signUpWithEmail("nguyenhuy@gmail.com", "123456") {
                    Log.d(TAG, "signUpWithEmail: success is " + it)
                }
            }
            button2.setOnClickListener {
                viewModel.signInWithEmail("nguyenhuy@gmail.com", "123456") {
                    Log.d(TAG, "signInWithEmail: success is " + it)
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainActivityLogging"
    }
}