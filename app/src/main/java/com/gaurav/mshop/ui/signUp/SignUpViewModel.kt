package com.gaurav.mshop.ui.signUp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaurav.mshop.entity.User
import com.gaurav.mshop.repository.UserRepository
import kotlinx.coroutines.launch

class SignUpViewModel(val repository: UserRepository) : ViewModel() {


    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text

    fun registerUser(user: User) {
        viewModelScope.launch {
            try {
                val text = repository.userRegister(user)
                _text.value = text.toString()
            } catch (ex: Exception) {
                _text.value = ""
                Log.d("vm", ex.toString())
            }
        }
    }

}