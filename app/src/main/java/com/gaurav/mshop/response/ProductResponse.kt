package com.gaurav.mshop.response

import com.gaurav.mshop.entity.Product

class ProductResponse(
    val success: Boolean? = null,
    val data: MutableList<Product>? = null
)