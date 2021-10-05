package com.gaurav.mshop.ui.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gaurav.mshop.repository.UserRepository

class SignUpViewModelFactory(val repository: UserRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SignUpViewModel(repository) as T
    }
}