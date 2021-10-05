package com.gaurav.mshop.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cart (
    @PrimaryKey()
    var _id: String,
    var productImage: String? = null,
    var productName: String? = null,
    var productPrice: String? = null,
    var productQuantity: String? = null,
)