package com.gaurav.mshop.api

import com.gaurav.mshop.response.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductAPI {
    @GET("product/show/")
    suspend fun getProducts():Response<ProductResponse>

    @GET("product/show/{id}")
    suspend fun showSingleProduct(
        @Path("id") id:String
    ) : Response<ProductResponse>
}