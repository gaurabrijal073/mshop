package com.gaurav.mshop.ui.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gaurav.mshop.repository.UserRepository

class SplashViewModelFactory(val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SplashViewModel(repository) as T
    }

}