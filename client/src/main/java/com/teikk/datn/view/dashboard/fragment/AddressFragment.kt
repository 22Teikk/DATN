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
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
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

    fun fetchLatLngFromPlaceId(placeId: String, onResult: (Double, Double) -> Unit, onError: (Exception) -> Unit) {
        val placesClient: PlacesClient = Places.createClient(context)

        val placeFields = listOf(Place.Field.LAT_LNG) // Chỉ yêu cầu LatLng
        val request = FetchPlaceRequest.builder(placeId, placeFields).build()

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                val place = response.place
                val latLng = place.latLng
                if (latLng != null) {
                    onResult(latLng.latitude, latLng.longitude)
                } else {
                    onError(Exception("LatLng is null"))
                }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
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
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                Log.i("skjdfhajksdfhjkadsfhk", "An error occurred: ${place.id.toString()}")
                place.id?.let {
                    fetchLatLngFromPlaceId(
                        it,
                        onResult = { latitude, longitude ->
                            lat = latitude
                            long = longitude
                            mapGG.apply {
                                moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(latitude, longitude), 18f
                                    )
                                )
                                marker = addMarker(
                                    MarkerOptions().position(
                                        LatLng(latitude, longitude)
                                    )
                                )!!
                            }
                            binding.edtAddress.setText(requireContext().getAddressByLocation(lat, long))

                        },
                        onError = { error ->
                            Log.e("PlaceInfo", "Error fetching place: ${error.message}")
                        }
                    )
                }

            }

            override fun onError(status: Status) {
                Toast.makeText(requireContext(), "Something error occurred", Toast.LENGTH_SHORT).show()
                // TODO: Handle the error.
                Log.i("skjdfhajksdfhjkadsfhk", "An error occurred: $status")
            }
        })
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

        binding.icLocation.setOnClickListener {
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