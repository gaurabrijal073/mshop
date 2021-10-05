package com.gaurav.mshop.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gaurav.mshop.dao.ProductDao
import com.gaurav.mshop.entity.Product

@Database(entities = [(Product::class)], version = 1)
abstract class ProductDB : RoomDatabase() {
    abstract fun getProductDAO() : ProductDao

    companion object {
        @Volatile
        private var productInstance: ProductDB? = null

        fun getProductInstance(context: Context) : ProductDB {
            if (productInstance == null){
                synchronized(ProductDB::class){
                    productInstance = buildDatabase(context)
                }
            }
            return productInstance!!
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ProductDB::class.java,
                "ProductDatabase"
            ).build()
    }
}