package com.gaurav.mshop.ui.login

import android.content.Context.MODE_PRIVATE
import android.content.Context.SENSOR_SERVICE
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.gaurav.mshop.R
import com.gaurav.mshop.api.ServiceBuilder
import com.gaurav.mshop.entity.User
import com.gaurav.mshop.repository.UserRepository
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment(), SensorEventListener {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvSignUp: TextView
    private lateinit var linearLayout: ConstraintLayout

    private lateinit var sensorManager: SensorManager
    private var sensor : Sensor?= null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sensorManager = activity?.getSystemService(SENSOR_SERVICE) as SensorManager

        val repository = UserRepository()
        val viewModelFactory = LoginViewModelFactory(repository)
        loginViewModel =
            ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_login, container, false)

        etEmail = root.findViewById(R.id.etLoginEmail)
        etPassword = root.findViewById(R.id.etLoginPassword)
        btnLogin = root.findViewById(R.id.btnLogin)
        tvSignUp = root.findViewById(R.id.tvCreateAccount)
        linearLayout = root.findViewById(R.id.linear_layout)

//        loginViewModel.user.observe(viewLifecycleOwner, Observer { user ->
//            if (user == null) {
//                val snackBar = Snackbar.make(
//                    requireActivity().findViewById(android.R.id.content),
//                    "Invalid User!!!",
//                    Snackbar.LENGTH_LONG
//                )
//                snackBar.setBackgroundTint(Color.parseColor("#F8A3A3"))
//                snackBar.show()
//            } else {
//                ServiceBuilder.user = user
//                saveSharedPreferences(user)
//                view?.findNavController()?.navigate(R.id.userProfileFragment)
//                loginNotification()
//            }
//        })

        btnLogin.setOnClickListener {
            val emailAddress = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val snackBar = Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                "Login Successful",
                Snackbar.LENGTH_LONG
            )
            snackBar.setBackgroundTint(Color.parseColor("#F8A3A3"))
            snackBar.show()
            Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
            view?.findNavController()?.navigate(R.id.navigation_home)

        }

        tvSignUp.setOnClickListener {
            view?.findNavController()?.navigate(R.id.navigation_notifications)
        }


        if(!checkSensor())
            return root
        else{
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
            sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL)
        }

        return root
    }

    private fun loginNotification() {
        val notificationManager = NotificationManagerCompat.from(requireContext())
        val notificationChannels = NotificationChannel(requireContext())

        notificationChannels.createNotificationChannels()

        val notification =
            NotificationCompat.Builder(requireContext(), notificationChannels.loginChannel)
                .setSmallIcon(R.drawable.bakerylogo)
//            .setSmallIcon(R.id.icon_only)
                .setContentTitle("User Login")
                .setContentText("User login into this application.")
                .setColor(Color.RED)
                .build()

        notificationManager.notify(1, notification)
    }

    private fun saveSharedPreferences(user: User) {
        val saveEmailAddress = etEmail.text.toString()
        val savePassword = etPassword.text.toString()
        val sharedPreference =
            requireActivity().getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val editor = sharedPreference.edit()

        editor.putString("emailAddress", saveEmailAddress)
        editor.putString("password", savePassword)
        editor.putString("user", user.toString())
        editor.apply()
        Toast.makeText(context, "Saved Preferences!!", Toast.LENGTH_SHORT).show()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val values = event!!.values[0]
        if(values>23000){
            linearLayout.setBackgroundColor(Color.WHITE)
        }
        else{
            linearLayout.setBackgroundColor(Color.parseColor("#E4CACB"))
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    private fun checkSensor(): Boolean {
        var flag = true
        if(sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)==null){
            flag = false
        }
        return flag
    }

    private fun clearText(){
        etEmail.setText("")
        etPassword.setText("")
    }

    private fun setValidation() : Boolean{
        var flag = true
        when {
            TextUtils.isEmpty(etEmail.text) -> {
                etEmail.error = "Please enter email address."
                etEmail.requestFocus()
                flag = false
            }
            TextUtils.isEmpty(etPassword.text) -> {
                etPassword.error = "Please enter password."
                etPassword.requestFocus()
                flag = false
            }
        }
        return flag
    }
}