package com.teikk.datn.view.permission.fragment

import android.app.Activity.RESULT_OK
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.fragment.findNavController
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.base.SharedPreferenceUtils
import com.teikk.datn.databinding.FragmentPermissionBackgroundBinding
import com.teikk.datn.view.authentication.AuthenticationActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PermissionBackgroundFragment : BaseFragment<FragmentPermissionBackgroundBinding>() {
    @Inject
    lateinit var sharedPreferences: SharedPreferenceUtils
    override fun getLayoutResId(): Int {
        return R.layout.fragment_permission_background
    }

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
        if (result) {
            sharedPreferences.putBooleanValue("isFirstTime", false)
            openActivity(AuthenticationActivity::class.java, true)
        }
    }

    override fun init() {
        if (checkPermissionBackground()) {
            sharedPreferences.putBooleanValue("isFirstTime", false)
            openActivity(AuthenticationActivity::class.java, true)
        }
    }

    override fun initEvent() {
        binding.btnAllow.setOnClickListener {
            if (checkPermissionBackground()) {
                sharedPreferences.putBooleanValue("isFirstTime", false)
                openActivity(AuthenticationActivity::class.java, true)
            } else {
                permissionLauncher.launch(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
        }
    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {

            }
        }

    fun checkPermissionBackground(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            if ((ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED)
            ) return false
        return true
    }
}