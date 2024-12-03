package com.teikk.datn.view.authentication.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.base.setSafeOnClickListener
import com.teikk.datn.base.showShortToast
import com.teikk.datn.databinding.FragmentFirstLoginBinding
import com.teikk.datn.utils.Resource
import com.teikk.datn.utils.getAddressByLocation
import com.teikk.datn.utils.uriToFile
import com.teikk.datn.view.authentication.AuthenticationViewModel
import com.teikk.datn.view.dashboard.DashBoardActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstLoginFragment : BaseFragment<FragmentFirstLoginBinding>() {
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }
    private val viewModel: AuthenticationViewModel by activityViewModels()
    private var imgUri: Uri? = null
    private var lat: Double = 0.0
    private var long: Double = 0.0
    override fun getLayoutResId(): Int {
        return R.layout.fragment_first_login
    }

    @SuppressLint("MissingPermission")
    override fun init() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    lat = location.latitude
                    long = location.longitude
                }
            }
            .addOnFailureListener { e ->

            }
    }

    @SuppressLint("MissingPermission")
    override fun initEvent() {
        with(binding) {
            imgEditAvatar.setSafeOnClickListener {
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                }
                arlBackground.launch(intent)
            }
            btnCreate.setSafeOnClickListener {
                if (imgUri != null) {
                    requireContext().uriToFile(imgUri!!)?.let { it1 ->
                        viewModel.uploadFile(it1) { url ->
                            if (url != null) {
                                val user = viewModel.user.value!!.data!!.copy(
                                    name = edtName.text.toString(),
                                    phone = edtPhone.text.toString(),
                                    lat = lat,
                                    longtitude = long,
                                    address = requireContext().getAddressByLocation(lat, long),
                                    imageUrl = url
                                )
                                viewModel.updateUser(user)
                            }
                        }
                    }
                } else {
                    Log.d("swlkdjfglksdjfgklsjdfkl", long.toString())
                    val user = viewModel.user.value!!.data!!.copy(
                        name = edtName.text.toString(),
                        phone = edtPhone.text.toString(),
                        lat = lat,
                        longtitude = long,
                        address = requireContext().getAddressByLocation(lat, long),
                    )
                    Log.d("swlkdjfglksdjfgklsjdfkl", user.toString())
                    viewModel.updateUser(user)
                }
            }
        }
    }

    override fun initObserver() {
        viewModel.userSuccess.observe(this) {
            when (it) {
                is Resource.Success -> {
                    Log.d("sjkhsajklfahsdf", "Come here")
                    openActivity(DashBoardActivity::class.java, isClearBackStack = true)
                }
                is Resource.Error -> {
                    showShortToast(it.message.toString())
                }
                is Resource.Loading -> {

                }
            }
        }
    }

    val arlBackground =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    imgUri = data.data
                    Glide.with(this).load(imgUri).into(binding.imgAvatar)
                }
            }
        }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {

            }
        }
}