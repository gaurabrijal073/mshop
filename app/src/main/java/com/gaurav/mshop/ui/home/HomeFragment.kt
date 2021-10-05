package com.gaurav.mshop.ui.home

import android.app.Activity
import android.content.Context.SENSOR_SERVICE
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gaurav.mshop.R
import com.gaurav.mshop.adapter.ProductAdapter
import com.gaurav.mshop.database.ProductDB
import com.gaurav.mshop.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel

class HomeFragment : Fragment(), SensorEventListener {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var productRecyclerView: RecyclerView

    private lateinit var imgSlider: ImageSlider

    private lateinit var adapter: ProductAdapter

    private lateinit var sensorManager: SensorManager
    private var sensor : Sensor?= null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val productDao = ProductDB.getProductInstance(requireContext()).getProductDAO()
        val repository = ProductRepository(productDao)
        val viewModelFactory = HomeViewModelFactory(repository)
        homeViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        productRecyclerView = root.findViewById(R.id.productRecyclerView)
        homeViewModel.showProducts()

        homeViewModel.text.observe(viewLifecycleOwner, Observer { lstProducts ->
            adapter.setList(lstProducts)
        })


        adapter = ProductAdapter(requireContext())
        productRecyclerView.layoutManager = GridLayoutManager(context, 2)
        productRecyclerView.adapter = adapter

        checkRunTimePermission()
//        loadProducts()
//        loadProductsFromRoomDatabase()

        imgSlider = root.findViewById(R.id.imgSlider)

        // Slide Show images
        val slideModels: MutableList<SlideModel> = ArrayList()
        slideModels.add(SlideModel(R.drawable.hbbaked, ScaleTypes.FIT))
        slideModels.add(SlideModel(R.drawable.cake, ScaleTypes.FIT))
        slideModels.add(SlideModel(R.drawable.baked, ScaleTypes.FIT))
        imgSlider.setImageList(slideModels)

        sensorManager = activity?.getSystemService(SENSOR_SERVICE) as SensorManager

        if(!checkSensor())
            return root
        else{
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
            sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL)
        }
        

        return root
    }

    private fun checkSensor(): Boolean {
        var flag = true
        if(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) == null){
            flag = flag
        }
        return flag
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val values = event!!.values[1]
        if(values>1){
            activity?.finish()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    private fun loadProductsFromRoomDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            val productDao = ProductDB.getProductInstance(requireContext()).getProductDAO()
            // Fetch data from room database
            val lstProducts = productDao.getProduct()
            withContext(Dispatchers.Main) {
                adapter.setList(lstProducts)
//                        productRecyclerView.adapter = ProductAdapter(response.data!!, requireContext())
////                            context?.let { ProductAdapter(response.data!!, it) }
//                        productRecyclerView.layoutManager = LinearLayoutManager(context)
            }

        }
    }

    private val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.CALL_PHONE
    )

    private fun checkRunTimePermission() {
        if (!hasPermission()) {
            requestPermission()
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(context as Activity, permissions, 1)
    }

    private fun hasPermission(): Boolean {
        var hasPermission = true
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                hasPermission = false
                break
            }
        }
        return hasPermission
    }
}