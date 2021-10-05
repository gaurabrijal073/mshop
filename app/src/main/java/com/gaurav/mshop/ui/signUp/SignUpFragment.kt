package com.gaurav.mshop.ui.signUp

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.gaurav.mshop.R
import com.gaurav.mshop.entity.User
import com.google.android.material.snackbar.Snackbar
import com.gaurav.mshop.repository.UserRepository

class SignUpFragment : Fragment() {
    private lateinit var signUpViewModel: SignUpViewModel

    private lateinit var etFullName : EditText
    private lateinit var etSignUpEmail : EditText
    private lateinit var etSignUpPassword : EditText
    private lateinit var etConfirmSignUpPassword : EditText
    private lateinit var etMobileNumber : EditText
    private lateinit var etAddress : EditText
    private lateinit var btnSignUp : Button

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val repository = UserRepository()
        val viewModelFactory = SignUpViewModelFactory(repository)

        signUpViewModel =
                ViewModelProvider(this, viewModelFactory).get(SignUpViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_sign_up, container, false)

        etFullName = root.findViewById(R.id.etFullName)
        etSignUpEmail = root.findViewById(R.id.etSignUpEmail)
        etSignUpPassword = root.findViewById(R.id.etSignUpPassword)
        etConfirmSignUpPassword = root.findViewById(R.id.etConfirmSignUpPassword)
        etMobileNumber = root.findViewById(R.id.etMobileNumber)
        etAddress = root.findViewById(R.id.etAddress)
        btnSignUp = root.findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val fullName = etFullName.text.toString()
            val email = etSignUpEmail.text.toString()
            val password = etSignUpPassword.text.toString()
            val confirmPassword = etConfirmSignUpPassword.text.toString()
            val mobileNumber = etMobileNumber.text.toString()
            val address = etAddress.text.toString()

            if (password != confirmPassword){
                etSignUpPassword.error = "Password does not match"
                etSignUpPassword.setText("")
                etConfirmSignUpPassword.setText("")
                etSignUpPassword.requestFocus()
                return@setOnClickListener
            } else{
                val user = User(fullName = fullName, email = email, password = password, mobileNumber = mobileNumber, address = address)
                if (setValidation()){
                    signUpViewModel.registerUser(user)
                    clearText()
                }
            }
        }

        signUpViewModel.text.observe(viewLifecycleOwner, Observer {
            val snackBar = Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                "User registered successfully.",
                Snackbar.LENGTH_LONG
            )
            snackBar.setBackgroundTint(Color.parseColor("#F8A3A3"))
            snackBar.show()
            view?.findNavController()?.navigate(R.id.navigation_home)
        })
        return root
    }

    private fun clearText(){
        etFullName.setText("")
        etSignUpEmail.setText("")
        etSignUpPassword.setText("")
        etConfirmSignUpPassword.setText("")
        etAddress.setText("")
        etMobileNumber.setText("")
    }

    private fun setValidation() : Boolean{
        var flag = true
        when {
            TextUtils.isEmpty(etFullName.text) -> {
                etFullName.error = "Please enter your full name"
                etFullName.requestFocus()
                flag = false
            }
            TextUtils.isEmpty(etSignUpEmail.text) -> {
                etSignUpEmail.error = "Please provide email address.."
                etSignUpEmail.requestFocus()
                flag = false
            }
            TextUtils.isEmpty(etSignUpPassword.text) -> {
                etSignUpPassword.error = "Please enter password."
                etSignUpPassword.requestFocus()
                flag = false
            }
            TextUtils.isEmpty(etConfirmSignUpPassword.text) -> {
                etConfirmSignUpPassword.error = "Please enter confirm password."
                etConfirmSignUpPassword.requestFocus()
                flag = false
            }
            TextUtils.isEmpty(etMobileNumber.text) -> {
                etMobileNumber.error = "Please enter your mobile number."
                etMobileNumber.requestFocus()
                flag = false
            }
            TextUtils.isEmpty(etAddress.text) -> {
                etAddress.error = "Please enter your address."
                etAddress.requestFocus()
                flag = false
            }
        }
        return flag
    }
}