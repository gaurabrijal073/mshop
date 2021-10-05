package com.gaurav.mshop.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gaurav.mshop.entity.Cart

class CartAdapter (
    val context: Context
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>(){
    var lstCartItems = emptyList<Cart>()

    fun setList(list: List<Cart>) {
        lstCartItems = list
        notifyDataSetChanged()

//        var _id: String,
//        var productImage: String? = null,
//        var productName: String? = null,
//        var productPrice: String? = null,
//        var productQuantity: String? = null,

    }

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}

