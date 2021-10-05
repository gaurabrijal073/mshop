package com.gaurav.mshop.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gaurav.mshop.R
import com.gaurav.mshop.api.ServiceBuilder
import com.gaurav.mshop.entity.Product
import com.gaurav.mshop.ui.home.ProductDetailsActivity

class ProductAdapter(

    val context: Context

) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    var lstProduct = emptyList<Product>()

    fun setList(list: List<Product>) {
        lstProduct = list
        notifyDataSetChanged()
    }

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImage: ImageButton = view.findViewById(R.id.imgProduct)
        val productName: TextView = view.findViewById(R.id.tvProductName)
        val productPrice: TextView = view.findViewById(R.id.tvProductPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_layout, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = lstProduct[position]
        holder.productName.text = product.productName
        holder.productPrice.text = product.productPrice

        val imagePath = ServiceBuilder.loadImagePath() + product.productImage!!.replace("\\", "/")

        Glide.with(context)
            .load(imagePath)
            .fitCenter()
            .into(holder.productImage)

        holder.productImage.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("product", product._id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return lstProduct.size
    }
}