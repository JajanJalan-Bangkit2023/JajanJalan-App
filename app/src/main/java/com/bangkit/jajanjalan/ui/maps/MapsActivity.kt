package com.bangkit.jajanjalan.ui.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.bangkit.jajanjalan.R
import com.bangkit.jajanjalan.data.Result

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.bangkit.jajanjalan.databinding.ActivityMapsBinding
import com.bangkit.jajanjalan.util.hide
import com.bangkit.jajanjalan.util.show
import com.bangkit.jajanjalan.util.toast
import com.bangkit.jajanjalan.util.vectorToBitmap
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel by viewModels<MapsViewModel>()
    private lateinit var client: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        client = LocationServices.getFusedLocationProviderClient(this)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private fun getMyLastLocation() {
        if     (ActivityCompat.checkSelfPermission(
                this@MapsActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this@MapsActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            client.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    mMap.isMyLocationEnabled = true
                    moveCameraToMyLocation(location)
                } else {
                    toast("Location not found")
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }


    @SuppressLint("SuspiciousIndentation")
    private fun moveCameraToMyLocation(location: Location) {
     val myLocation = LatLng(location.latitude, location.longitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 11f))
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getMyLastLocation()
        observeSeller()
    }

    private fun observeSeller() {
        viewModel.getAllPenjual().observe(this) {
            when(it) {
                Result.Loading -> {
                    binding.progressBar.show()
                }
                is Result.Success -> {
                    binding.progressBar.hide()
                    Log.d(TAG, "ResponseSeller: ${it.data.data}")
                    it.data.data?.penjual?.forEach { seller ->
                        val latLng = LatLng(seller.lat!!, seller.lon!!)
                        mMap?.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(seller.name)
                                .snippet(seller.address)
                                .icon(vectorToBitmap(R.drawable.ic_fastfood, resources.getColor(R.color.blue), this))
                        )
                    }
                }
                is Result.Error -> {
                    binding.progressBar.hide()
                    toast(it.error)
                    Log.e(TAG, "observeSeller: ${it.error}")
                }
            }
        }
    }


    companion object {
        private const val TAG = "MapsActivity"
    }
}