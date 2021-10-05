package com.gaurav.mshop.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gaurav.mshop.entity.Product

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    // For displaying product
    @Query("SELECT * FROM Product")
    suspend fun getProduct() : MutableList<Product>
}