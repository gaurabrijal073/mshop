package com.gaurav.mshop.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaurav.mshop.entity.Product
import com.gaurav.mshop.repository.ProductRepository
import kotlinx.coroutines.launch

class HomeViewModel(val repository: ProductRepository) : ViewModel() {

    private val _text = MutableLiveData<MutableList<Product>>()
//    val text: LiveData<String> = _text
    val text: LiveData<MutableList<Product>>
    get() = _text

    fun showProducts(){
        viewModelScope.launch {

            // fetch data from api
                val text = repository.displayProduct()
                _text.value = text!!
        }
    }
}