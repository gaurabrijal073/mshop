package com.gaurav.mshop.repository

import com.gaurav.mshop.api.ApiRequest
import com.gaurav.mshop.api.ServiceBuilder
import com.gaurav.mshop.api.UserAPI
import com.gaurav.mshop.entity.User
import okhttp3.MultipartBody

class UserRepository : ApiRequest() {
    // create retrofit instance of user api
    private val userAPI = ServiceBuilder.buildService(UserAPI::class.java)

    // for user register
//    suspend fun registerUser(user: User): LoginResponse {
//        return apiRequest {
//            userAPI.registerUser(user)
//        }
//    }

    //  for MVVM architecture
    suspend fun userRegister(user: User): Boolean {
        val response = apiRequest { userAPI.registerUser(user) }
        return response.success == true
    }

    // for user login
//    suspend fun loginUser(email: String, password: String): LoginResponse {
//        return apiRequest {
//            userAPI.loginUser(email, password)
//        }
//    }

    //  for MVVM architecture
    suspend fun userLogin(email: String, password: String): User? {
        val response = apiRequest { userAPI.loginUser(email, password) }
        ServiceBuilder.token = "Bearer ${response.token}"
        return response.user
//        return if (response.success == true) response.token!! else ""
    }

//    For image upload
    suspend fun uploadImage(id: String, body: MultipartBody.Part): String? {
    val response = apiRequest {
        userAPI.uploadImage(ServiceBuilder.token!!, id, body)
    }
    return response.data
}
}