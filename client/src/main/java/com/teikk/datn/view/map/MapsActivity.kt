package com.teikk.datn.view.map

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.teikk.datn.R
import com.teikk.datn.base.BaseActivity
import com.teikk.datn.data.model.Order
import com.teikk.datn.data.model.UserProfile
import com.teikk.datn.databinding.ActivityMapsBinding
import com.teikk.datn.utils.drawBitmapIntoVector
import com.teikk.datn.utils.getAddressByLocation
import com.teikk.datn.utils.roundBitmap
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : BaseActivity<ActivityMapsBinding>(), OnMapReadyCallback {
    private val viewModel by viewModels<MapViewModel>()
    private lateinit var user: UserProfile
    private lateinit var order: Order
    private lateinit var mapGG: GoogleMap
    private var myMarker: Marker? = null
    private val bitmap: Bitmap by lazy {
        Bitmap.createBitmap(
            350,
            350,
            Bitmap.Config.ARGB_8888
        )
    }

    private val myVectorLocation: Drawable by lazy {
        ContextCompat.getDrawable(this, R.drawable.icon_marker)!!
    }
    private val vectorDrawable: Drawable by lazy {
        ContextCompat.getDrawable(this, R.drawable.icon_radar)!!
    }
    override fun getLayoutResId(): Int {
        return R.layout.activity_maps
    }

    override fun init() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        user = intent.getSerializableExtra("user") as UserProfile
        Log.d("Sdfkjahsdfjkasd", getAddressByLocation(user.lat, user.longtitude))
        Log.d("Sdfkjahsdfjkasd", user.lat.toString())
        Log.d("Sdfkjahsdfjkasd", user.longtitude.toString())

        order = intent.getSerializableExtra("order") as Order
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun getMyLocation() {
        mapGG.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    user.lat,
                    user.longtitude,
                ), 18f
            )
        )
    }

    private fun viewMap() {
        mapGG.uiSettings.isCompassEnabled = false
        mapGG.uiSettings.isMyLocationButtonEnabled = true
        mapGG.uiSettings.isMapToolbarEnabled = false
        mapGG.uiSettings.isZoomControlsEnabled = false
        mapGG.apply {
            val canvas = Canvas(bitmap)
            vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
            vectorDrawable.draw(canvas)
            myMarker = this.addMarker(
                MarkerOptions().position(
                    LatLng(0.0, 0.0)
                )
                    .zIndex(0f)
            )!!.apply {
                alpha = 0f
                setIcon(BitmapDescriptorFactory.fromBitmap(bitmap))
                isFlat = true
                setAnchor(0.5f, 0.5f)
            }
        }
    }

    private fun eventMap() {
        with(binding) {
            btnBack.setOnClickListener {
                finish()
            }
            imgMapType.setOnClickListener {
                if (viewModel.mapType.value == 4) {
                    viewModel.changeTypeMap(1)
                } else {
                    viewModel.changeTypeMap(viewModel.mapType.value!! + 1)
                }
            }
            imgMyLocation.setOnClickListener {
                getMyLocation()
            }
            imgEmployeeLocation.setOnClickListener {
                mapGG.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        myMarker!!.position, 18f
                    )
                )
            }
        }
    }

    private fun observerMap() {
        viewModel.mapType.observe(this) {
            mapGG.mapType = it
        }
        viewModel.socketManager.onGetLocation {
            Log.d("ajkdfhjkasdfhasdf", it.toString())
            Log.d("sdjkafh", "Client: " +  order.id)
            Log.d("sdjkafh", "Employee: " + it.orderId)
            Log.d("ajkdfhjkasdfhasdf", (order.id == it.orderId).toString())
            Log.d("ajkdfhjkasdfhasdf", (it.uid == viewModel.uid).toString())
            if (it.uid == viewModel.uid && it.orderId == order.id) {
                Log.d("EMPLOYEE POSITION", "Come here")
                createMyMarker(it.latitude, it.longitude)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mapGG = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(user.lat, user.longtitude)
        mapGG.addMarker(MarkerOptions().position(sydney).title("My Location"))
        viewMap()
        getMyLocation()
        observerMap()
        eventMap()
    }

    private fun createMyMarker(lat: Double, long: Double) {
        mapGG.apply {
            Glide.with(this@MapsActivity)
                .asBitmap()
                .load(R.drawable.app_icon)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .override(80, 80)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(
                            drawBitmapIntoVector(
                                roundBitmap(resource, 80, 80),
                                myVectorLocation
                            )
                        )
                        myMarker?.apply {
                            position = LatLng(lat, long)
                            zIndex = 2f
                            alpha = 1f
                        }!!.apply {
                            setIcon(bitmapDescriptor)
                        }
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }
    }
}