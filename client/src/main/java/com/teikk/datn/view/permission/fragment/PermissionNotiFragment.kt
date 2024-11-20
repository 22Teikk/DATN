package com.teikk.datn.view.permission.fragment

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.databinding.FragmentPermissionNotiBinding

class PermissionNotiFragment : BaseFragment<FragmentPermissionNotiBinding>() {
    private val REQUEST_POST_NOTIFICATIONS_CODE = 1111
    override fun getLayoutResId(): Int {
        return R.layout.fragment_permission_noti
    }

    override fun init() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            findNavController().navigate(R.id.action_permissionNotiFragment_to_permissionLocationFragment)
        }
    }

    override fun initEvent() {
        binding.btnAllow.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d("djkfhsjkd", "Permission granted")
                    findNavController().navigate(R.id.action_permissionNotiFragment_to_permissionLocationFragment)
                } else {
                    Log.d("djkfhsjkd", "Permission denied")
                    permissionLauncher.launch(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                }
            } else {
                findNavController().navigate(R.id.action_permissionNotiFragment_to_permissionLocationFragment)
            }
        }
    }

    val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            findNavController().navigate(R.id.action_permissionNotiFragment_to_permissionLocationFragment)
        }
    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {

            }
        }
}