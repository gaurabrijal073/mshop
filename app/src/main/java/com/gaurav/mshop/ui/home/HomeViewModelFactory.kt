package com.gaurav.mshop.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gaurav.mshop.repository.ProductRepository

class HomeViewModelFactory (val repository: ProductRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}