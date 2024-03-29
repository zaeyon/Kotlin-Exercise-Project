package com.jaeyeon.expeditiouscopyorderapp.ui.mySurrounding

import android.content.Context.LOCATION_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jaeyeon.expeditiouscopyorderapp.MainActivity
import com.jaeyeon.expeditiouscopyorderapp.R
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.util.FusedLocationSource

class MySurroundingFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mySurroundingViewModel: MySurroundingViewModel
    private lateinit var locationSource : FusedLocationSource

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mySurroundingViewModel =
            ViewModelProviders.of(this).get(MySurroundingViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_my_surrounding, container, false)

        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)


        return root
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions,
                grantResults)) {
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        val uiSettings = naverMap.uiSettings
        uiSettings.isZoomControlEnabled = false
        uiSettings.isCompassEnabled = false
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Face
        val userLatitude = MainActivity.accUserLatitude.toDouble()
        val userLongitude = MainActivity.accUserLongitude.toDouble()

        val earlyCameraPosition = CameraPosition(LatLng(userLatitude, userLongitude), 14.0)
        val earlyCameraUpdate = CameraUpdate.toCameraPosition(earlyCameraPosition)
        naverMap.moveCamera(earlyCameraUpdate)

    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }



}