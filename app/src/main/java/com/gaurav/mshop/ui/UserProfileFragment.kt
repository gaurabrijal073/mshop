package com.gaurav.mshop.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.gaurav.mshop.R
import com.gaurav.mshop.api.ServiceBuilder
import com.gaurav.mshop.repository.UserRepository
import com.gaurav.mshop.ui.login.LoginFragment
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class UserProfileFragment : Fragment(),SensorEventListener {

    private lateinit var tvFullName: TextView
    private lateinit var tvEmailAddress: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvMobileNumber: TextView
    private lateinit var imgProfile: CircleImageView
//    private lateinit var userProfileViewModel: UserProfileViewModel
    private lateinit var btnLogout: Button

    private lateinit var sensorManager: SensorManager
    private var sensor : Sensor?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)
        Log.d("Service", (ServiceBuilder.user != null).toString())

        if (ServiceBuilder.user == null) {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.nav_host_fragment, LoginFragment())
                commit()
            }
        }

        tvFullName = view.findViewById(R.id.tvFullName)
        tvEmailAddress = view.findViewById(R.id.tvUserEmail)
        tvAddress = view.findViewById(R.id.tvAddress)
        tvMobileNumber = view.findViewById(R.id.tvMobileNumber)
        imgProfile = view.findViewById(R.id.imgProfile)
        btnLogout = view.findViewById(R.id.btnLogout)

        imgProfile.setOnClickListener {
            loadPopUpMenu()
        }

        tvFullName.text = ServiceBuilder.user?.fullName
        tvEmailAddress.text = ServiceBuilder.user?.email
        tvMobileNumber.text = ServiceBuilder.user?.mobileNumber
        tvAddress.text = ServiceBuilder.user?.address
        sensorManager = activity?.getSystemService(SENSOR_SERVICE) as SensorManager

        Glide.with(requireActivity())
            .load((ServiceBuilder.loadImagePath()+ServiceBuilder.user?.userProfile).replace("\\","/"))
            .into(imgProfile)




        btnLogout.setOnClickListener {

            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Alert Message")
            builder.setMessage("Are you sure want to logout ?")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            builder.setPositiveButton("Yes") { _, _ ->
                val sharedPref =
                    requireContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
                sharedPref.edit().clear().apply()
                ServiceBuilder.user = null
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.nav_host_fragment, LoginFragment())
                    commit()
                }
                Toast.makeText(activity, "Logout", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("No") { _, _ ->
                Toast.makeText(activity, "Logout Cancel", Toast.LENGTH_SHORT).show()
            }
            builder.setNeutralButton("Cancel") { _, _ ->
                Toast.makeText(activity, "Action not perform", Toast.LENGTH_SHORT).show()
            }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }

        if(!checkValue())
            return view

        else{
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
            sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL)
        }

        return view
    }

    private fun checkValue(): Boolean {
        var flag = true
        if(sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)==null){
            flag = false
        }
        return flag
    }

    // Load pop up menu
    private fun loadPopUpMenu() {
        val popupMenu = PopupMenu(context, imgProfile)
        popupMenu.menuInflater.inflate(R.menu.gallery_camera, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuCamera ->
                    openCamera()
                R.id.menuGallery ->
                    openGallery()
            }
            true
        }
        popupMenu.show()
    }

    private var REQUEST_GALLERY_CODE = 0
    private var REQUEST_CAMERA_CODE = 1
    private var imageUrl: String? = null

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY_CODE)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CAMERA_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_CODE && data != null) {
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val contentResolver = activity?.contentResolver
                val cursor =
                    contentResolver?.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                imageUrl = cursor.getString(columnIndex)
                imgProfile.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                cursor.close()
            } else if (requestCode == REQUEST_CAMERA_CODE && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                imageUrl = file!!.absolutePath
                imgProfile.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
            }
        }
        ServiceBuilder.user?._id?.let { uploadImage(it) }
    }

    private fun bitmapToFile(
        bitmap: Bitmap,
        fileNameToSave: String
    ): File? {
        var file: File? = null
        return try {
            file = File(
                activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    .toString() + File.separator + fileNameToSave
            )
            file.createNewFile()
            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitMapData = bos.toByteArray()
            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitMapData)
            fos.flush()
            fos.close()
            file
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }

    private fun uploadImage(userId: String) {
        if (imageUrl != null) {
            val file = File(imageUrl!!)
            val reqFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val body =
                MultipartBody.Part.createFormData("image", file.name, reqFile)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userRepo = UserRepository()
                    val response = userRepo.uploadImage(userId, body)
                    if (response != "" || response !=null) {
                        withContext(Dispatchers.Main) {
                            ServiceBuilder.user?.userProfile = response

                            Glide.with(requireActivity())
                                .load((ServiceBuilder.loadImagePath()+ServiceBuilder.user?.userProfile).replace("\\","/"))
                                .into(imgProfile)
                        }
                    }
                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.d("Mero Error ", ex.localizedMessage)
                        Toast.makeText(
                            context,
                            ex.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val values = event !!.values[0]
        if(values<=3){
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.nav_host_fragment, LoginFragment())
                commit()
                val sharedPref =
                    requireContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
                sharedPref.edit().clear().apply()
                ServiceBuilder.user = null
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}