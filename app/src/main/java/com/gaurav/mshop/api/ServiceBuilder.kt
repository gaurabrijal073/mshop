package com.gaurav.mshop.api

import com.gaurav.mshop.entity.User
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    // For Emulator
//    private const val BASE_URL = "http://10.0.2.2:9000/"

    // For Real Device
//    private const val BASE_URL = "http://192.168.1.70:9000/"

    // For Testing
    private const val BASE_URL = "http://localhost:9000/"
    var token: String? = null
    var user: User?=null
    private val okHttp = OkHttpClient.Builder()
    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp.build())

    // Create retrofit instance
    private val retrofit = retrofitBuilder.build()

    // Generic function
    fun <T> buildService(serviceType: Class<T>): T {
        return retrofit.create(serviceType)
    }

    // Load image path
    fun loadImagePath(): String {
        val arr = BASE_URL.split("/").toTypedArray()
        return arr[0] + "//" + arr[1] + arr[2] + "/"
    }
}