package com.gaurav.mshop.ui.splashscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gaurav.mshop.BottomNavActivity
import com.gaurav.mshop.R
import com.gaurav.mshop.api.ServiceBuilder
import com.gaurav.mshop.repository.UserRepository
import com.gaurav.mshop.ui.login.LoginViewModel
import com.gaurav.mshop.ui.login.LoginViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var password: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            startActivity(Intent(this@SplashActivity, BottomNavActivity::class.java))
            finish()
        }

        getSharedPref()
        val repository = UserRepository()
        val viewModelFactory = LoginViewModelFactory(repository)
        val loginViewModel =
            ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        loginViewModel.loginUser(email,password)

        loginViewModel.user.observe(this, Observer { user ->
            ServiceBuilder.user = user
        })
    }

    private fun getSharedPref() {
        val sharedPref = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        email = sharedPref?.getString("emailAddress", "").toString()
        password = sharedPref?.getString("password", "").toString()
    }
}