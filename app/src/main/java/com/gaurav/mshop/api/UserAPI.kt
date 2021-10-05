package com.gaurav.mshop.api

import com.gaurav.mshop.response.LoginResponse
import com.gaurav.mshop.entity.User
import com.gaurav.mshop.response.ImageResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserAPI {

    // for user registration
    @POST("user/register/")
    suspend fun registerUser(
            @Body user: User
    ) : Response<LoginResponse>

    // for user login
    @FormUrlEncoded
    @POST("user/login/")
    suspend fun loginUser(
            @Field("email") email: String,
            @Field("password") password: String
    ) : Response<LoginResponse>

    @Multipart
    @PUT("user/{id}/photo")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Part image: MultipartBody.Part
    ): Response<ImageResponse>
}