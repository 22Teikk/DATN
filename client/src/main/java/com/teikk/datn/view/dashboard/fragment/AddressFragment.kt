package com.teikk.datn.view.dashboard.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
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
import com.teikk.datn.utils.getAddressShareLocation
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddressFragment : BaseFragment<FragmentAddressBinding>(), OnMapReadyCallback {
    private lateinit var mapGG: GoogleMap


    private lateinit var marker: Marker


    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_address
    }

    override fun init() {
        val mapFragment = (R.id.map_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override val onBackPressedCallback: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }

    override fun onMapReady(googleMap: GoogleMap) {
        mapGG = googleMap
        binding.edtSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.icSearch.visibility = View.GONE
            }
        }

        binding.edtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                val location = binding.edtSearch.text.toString()

                val geocoder = Geocoder(requireContext())
                val addresses: List<Address>? = geocoder.getFromLocationName(location, 1)

                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    val latLng = LatLng(address.latitude, address.longitude)

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    mapGG.clear()
                    marker = mapGG.addMarker(
                        MarkerOptions().position(
                            latLng
                        )
                    )!!
                }
            } else {
                Toast.makeText(requireContext(), "Không tìm thấy địa điểm", Toast.LENGTH_SHORT)
                    .show()
            }
            true
        }
        binding.icLocation.setOnClickListener {
            binding.icSearch.visibility = View.VISIBLE
            binding.edtSearch.setText("")
            binding.edtSearch.clearFocus()
        }

        googleMap.uiSettings.isCompassEnabled = false
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = false

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mapGG.setOnMapClickListener { latLng ->
            mapGG.clear()
            marker = mapGG.addMarker(
                MarkerOptions().position(
                    latLng
                )
            )!!
            binding.edtAddress.setText(
                requireContext().getAddressShareLocation(
                    latLng.latitude,
                    latLng.longitude
                )
            )
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            mapGG.apply {
                moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            0.0,
                            0.0
                        ), 18f
                    )
                )
                marker = addMarker(
                    MarkerOptions().position(
                        LatLng(
                            0.0,
                            0.0
                        )
                    )
                )!!
            }
        }
    }
}