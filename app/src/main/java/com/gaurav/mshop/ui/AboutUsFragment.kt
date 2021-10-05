package com.gaurav.mshop.ui

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gaurav.mshop.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class AboutUsFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->

        val myBakeryLocation = LatLng(26.7361846, 86.9338991)
        googleMap.addMarker(MarkerOptions().position(myBakeryLocation).title("MyBakery"))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myBakeryLocation, 15F), 4000, null )
        googleMap.uiSettings.isZoomControlsEnabled = true
//        googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID;
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}