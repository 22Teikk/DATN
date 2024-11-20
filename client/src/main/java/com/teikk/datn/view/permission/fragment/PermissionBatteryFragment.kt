package com.teikk.datn.view.permission.fragment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.databinding.FragmentPermissionBatteryBinding

class PermissionBatteryFragment : BaseFragment<FragmentPermissionBatteryBinding>() {
    override fun getLayoutResId(): Int {
        return R.layout.fragment_permission_battery
    }

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            findNavController().navigate(R.id.action_permissionBatteryFragment_to_permissionBackgroundFragment)
        }
    }

    override fun init() {
        if (checkPermissionPower()) {
            findNavController().navigate(R.id.action_permissionBatteryFragment_to_permissionBackgroundFragment)
        }
    }

    override fun initEvent() {
        binding.btnAllow.setOnClickListener {
            if (checkPermissionPower()) {
                findNavController().navigate(R.id.action_permissionBatteryFragment_to_permissionBackgroundFragment)
            } else {
                val intent = Intent()
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:${requireContext().packageName}")
                permissionLauncher.launch(intent)
            }
        }
    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {

            }
        }

    private fun checkPermissionPower(): Boolean {
        val pm: PowerManager = requireContext().getSystemService(Context.POWER_SERVICE) as PowerManager
        return pm.isIgnoringBatteryOptimizations(requireContext().packageName)
    }
}