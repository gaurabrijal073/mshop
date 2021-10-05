package com.gaurav.mshop.repository

import android.util.Log
import com.gaurav.mshop.api.ApiRequest
import com.gaurav.mshop.api.ProductAPI
import com.gaurav.mshop.api.ServiceBuilder
import com.gaurav.mshop.dao.ProductDao
import com.gaurav.mshop.entity.Product
import com.gaurav.mshop.response.ProductResponse
import java.lang.Exception

class ProductRepository(private val productDao: ProductDao?=null) : ApiRequest() {
    private val productAPI = ServiceBuilder.buildService(ProductAPI::class.java)

    // to display all products
    suspend fun showProducts() : ProductResponse {
        return apiRequest {
            productAPI.getProducts()
        }
    }

    //  for MVVM architecture
    suspend fun displayProduct() : MutableList<Product>? {
//        val product = apiRequest { productAPI.getProducts() }
//        return product.data!!

        try {
            val response = apiRequest { productAPI.getProducts() }
            saveToRoom(response.data!!)
            return productDao?.getProduct()
        } catch (ex: Exception){
            Log.d("repo", ex.toString())
        }
        return productDao?.getProduct()
    }

    private suspend fun saveToRoom(products: MutableList<Product>) {
        for (product in products) {
            productDao?.insertProduct(product)
        }
    }

    suspend fun showSingleProduct(id:String) : ProductResponse {
        return apiRequest {
            productAPI.showSingleProduct(id)
        }
    }

}