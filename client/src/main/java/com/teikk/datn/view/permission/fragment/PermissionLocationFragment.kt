package com.teikk.datn.view.permission.fragment

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.tasks.Task
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.databinding.FragmentPermissionLocationBinding
import okhttp3.internal.checkOffsetAndCount

class PermissionLocationFragment : BaseFragment<FragmentPermissionLocationBinding>() {
    private val REQUEST_LOCATION_SETTING = 2222
    override fun getLayoutResId(): Int {
        return R.layout.fragment_permission_location
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            checkLocationSettings()
        }
    }

    override fun init() {
        if (checkPermissionLocation()) {
            checkLocationSettings()
        }
    }

    override fun initEvent() {
        binding.btnAllow.setOnClickListener {
            if (checkPermissionLocation()) {
                checkLocationSettings()
            } else {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun checkLocationSettings() {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(requireActivity())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            // Nếu cài đặt vị trí chính xác đã được bật, tiến hành đến màn hình tiếp theo
            findNavController().navigate(R.id.action_permissionLocationFragment_to_permissionBatteryFragment)
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Nếu cài đặt vị trí chưa được bật, hiển thị dialog yêu cầu bật vị trí
                try {
                    exception.startResolutionForResult(requireActivity(), REQUEST_LOCATION_SETTING)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Xử lý lỗi khi gửi intent
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOCATION_SETTING) {
            // Xử lý kết quả của dialog cài đặt vị trí
            if (resultCode == RESULT_OK) {
                findNavController().navigate(R.id.action_permissionLocationFragment_to_permissionBatteryFragment)
            } else {
                checkLocationSettings()
            }
        }
    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {

            }
        }

    fun checkPermissionLocation(): Boolean {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}