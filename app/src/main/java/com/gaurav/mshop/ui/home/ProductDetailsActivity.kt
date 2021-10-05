package com.gaurav.mshop.ui.home

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.gaurav.mshop.R
import com.gaurav.mshop.database.ProductDB
import com.gaurav.mshop.repository.ProductRepository
import com.gaurav.mshop.ui.login.NotificationChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var productImage: ImageView
    private lateinit var productName: TextView
    private lateinit var productPrice: TextView
    private lateinit var productDescription: TextView
    private lateinit var btnOrder: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        productImage = findViewById(R.id.productImageView)
        productName = findViewById(R.id.tvProductName)
        productPrice = findViewById(R.id.tvProductPrice)
        productDescription = findViewById(R.id.tvProductDescription)
        btnOrder = findViewById(R.id.btnOrder)

        btnOrder.setOnClickListener {
            orderNotification()
        }

        val intent = intent.getStringExtra("product")
        showSingleProduct(intent!!)
    }

    private fun orderNotification() {
        val notificationManager = NotificationManagerCompat.from(this)
        val notificationChannels = NotificationChannel(this)

        notificationChannels.createNotificationChannels()

        val notification =
            NotificationCompat.Builder(this, notificationChannels.loginChannel)
                .setSmallIcon(R.drawable.bakerylogo)
                .setContentTitle("Product Order")
                .setContentText("Your order has been placed.")
                .setColor(Color.RED)
                .build()

        notificationManager.notify(1, notification)
    }

    private fun showSingleProduct(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val instance = ProductDB.getProductInstance(this@ProductDetailsActivity)
            val response = ProductRepository(instance.getProductDAO()).showSingleProduct(id)
            try {
                if (response.success == true) {
                    withContext(Dispatchers.Main) {
                        val productData = response.data
                        productName.text = productData?.get(0)?.productName
                        productPrice.text = productData?.get(0)?.productPrice
                        productDescription.text = productData?.get(0)?.productDescription
                        val imagePath = "http://10.0.2.2:9000/" + productData?.get(0)?.productImage
                        Glide.with(applicationContext)
                            .load(imagePath.replace("\\","/"))
                            .into(productImage)
                    }
                }

            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ProductDetailsActivity, ex.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}