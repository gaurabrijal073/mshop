package com.gaurav.mshop.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaurav.mshop.entity.User
import com.gaurav.mshop.repository.UserRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModel(val repo : UserRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {

    }
    val text: LiveData<String> = _text
    private val _user = MutableLiveData<User?>()
    val user: MutableLiveData<User?> = _user

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                _user.value = repo.userLogin(email, password)

            } catch (ex: Exception) {
                _user.value = null
            }
        }
    }
}