package com.teikk.datn.view.authentication

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
//                viewModel.signUpWithEmail("nguyenhuy@gmail.com", "123456") {
//                    Log.d(TAG, "signUpWithEmail: success is " + it)
//                }
                viewModel.register("klasdfio2wueoisdn@gmail.com", "string")
            }
            button2.setOnClickListener {
//                viewModel.signInWithEmail("nguyenhuy@gmail.com", "123456") {
//                    Log.d(TAG, "signInWithEmail: success is " + it)
//                }
                viewModel.login("klasdfio2wueoisdn@gmail.com", "string")
            }
            button3.setOnClickListener {
                viewModel.fetchRoleData()
            }
        }
    }

    companion object {
        private const val TAG = "MainActivityLogging"
    }
}