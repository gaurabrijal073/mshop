package com.gaurav.mshop.response

import com.gaurav.mshop.entity.User

data class LoginResponse (
    val success : Boolean? = null,
    val token : String? = null,
    val user : User? = null
)