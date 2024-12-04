package com.teikk.datn.view.dashboard.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.teikk.datn.R
import com.teikk.datn.base.BaseFragment
import com.teikk.datn.databinding.FragmentAddressBinding
import com.teikk.datn.utils.getAddressByLocation
import com.teikk.datn.view.dashboard.DashBoardViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddressFragment : BaseFragment<FragmentAddressBinding>(), OnMapReadyCallback {
    private val viewModel by activityViewModels<DashBoardViewModel>()
    private lateinit var mapGG: GoogleMap


    private lateinit var marker: Marker
    private var lat: Double = 0.0
    private var long: Double = 0.0

    override fun getLayoutResId(): Int {
        return R.layout.fragment_address
    }

    override fun init() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun initEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnSave.setOnClickListener {
            val location = binding.edtAddress.text.toString()
            val userUpdate = viewModel.user.value?.data!!.copy(
                lat = lat,
                longtitude = long,
                address = location
            )
            viewModel.updateUser(userUpdate)
        }
    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }

    override fun onMapReady(googleMap: GoogleMap) {
        mapGG = googleMap
        viewModel.user.observe(viewLifecycleOwner) {
            lat = it.data?.lat!!
            long = it.data?.longtitude!!
            binding.edtAddress.setText(it.data?.address)
            mapGG.apply {
                moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            lat,
                            long
                        ), 18f
                    )
                )
                marker = addMarker(
                    MarkerOptions().position(
                        LatLng(
                            lat,
                            long
                        )
                    )
                )!!
            }
        }
        binding.edtSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.icSearch.visibility = View.GONE
            }
        }

        binding.edtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL ) {
                val location = binding.edtSearch.text.toString()
                Log.d("aksjdfhadjksfhh", location)
//
                val geocoder = Geocoder(requireContext())
                val addresses: List<Address>? = geocoder.getFromLocationName(location, 1)

                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    val latLng = LatLng(address.latitude, address.longitude)

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    binding.edtAddress.setText(requireContext().getAddressByLocation(latLng.latitude, latLng.longitude))

                    mapGG.clear()
                    marker = mapGG.addMarker(
                        MarkerOptions().position(
                            latLng
                        )
                    )!!
                } else {
                    // Xử lý trường hợp không tìm thấy địa điểm
                    Toast.makeText(requireContext(), "Không tìm thấy địa điểm", Toast.LENGTH_SHORT).show()
                }
                true
            } else {
                false
            }
        }

        binding.icLocation.setOnClickListener {
            binding.icSearch.visibility = View.VISIBLE
            binding.edtSearch.setText("")
            binding.edtSearch.clearFocus()
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@setOnClickListener
            }
        }

        googleMap.uiSettings.isCompassEnabled = false
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = false

        mapGG.setOnMapClickListener { latLng ->
            mapGG.clear()
            marker = mapGG.addMarker(
                MarkerOptions().position(
                    latLng
                )
            )!!
            lat = latLng.latitude
            long = latLng.longitude
            binding.edtAddress.setText(requireContext().getAddressByLocation(lat, long))
        }
    }
}