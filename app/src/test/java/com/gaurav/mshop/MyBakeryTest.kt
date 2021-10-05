package com.gaurav.mshop

import com.gaurav.mshop.entity.User
import com.gaurav.mshop.repository.ProductRepository
import com.gaurav.mshop.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class MyBakeryTest {
    private lateinit var userRepository: UserRepository
    private lateinit var productRepository: ProductRepository

    // --------------------------- User Testing --------------------------------
    // For Test Pass
    @Test
    fun checkValidLogin() = runBlocking {
        userRepository = UserRepository()
        val response = userRepository.userLogin("admin@gmail.com", "admin")
        val expectedResult = true
        val actualResult = response != null
        Assert.assertEquals(expectedResult, actualResult)
    }

    // For Test Fail
    @Test
    fun checkInValidLogin() = runBlocking {
        userRepository = UserRepository()
        val response = userRepository.userLogin("admin@gmail.com", "admin")
        val expectedResult = true
        val actualResult = response == null
        Assert.assertEquals(expectedResult, actualResult)
    }

    // For Test Pass
    @Test
    fun registerUser() = runBlocking {
        val user = User(
            fullName = "Ashok Rai",
            email = "rai573@gmail.com",
            password = "qwerty143",
            mobileNumber = "9807544517",
            address = "Lalitpur, Nepal"
        )
        userRepository = UserRepository()
        val response = userRepository.userRegister(user)
        val expectedResult = true
        val actualResult = response != null
        Assert.assertEquals(expectedResult, actualResult)
    }

    // For Test Fail
    @Test
    fun userNotRegistered() = runBlocking {
        val user = User(
            fullName = "Ashok Rai",
            email = "rai573@gmail.com",
            password = "qwerty143",
            mobileNumber = "9807544517",
            address = "Lalitpur, Nepal"
        )
        userRepository = UserRepository()
        val response = userRepository.userRegister(user)
        val expectedResult = true
        val actualResult = response == null
        Assert.assertEquals(expectedResult, actualResult)
    }


    // --------------------------- Product Testing --------------------------------
    // For Test Pass
    @Test
    fun loadProducts() = runBlocking {
        productRepository = ProductRepository()
        val response = productRepository.displayProduct()
        val expectedResult = true
        val actualResult = response?.size != 0
        Assert.assertEquals(expectedResult,actualResult)
    }

    // For Test Fail
    @Test
    fun productsNotLoad() = runBlocking {
        productRepository = ProductRepository()
        val response = productRepository.displayProduct()
        val expectedResult = true
        val actualResult = response?.size == 0
        Assert.assertEquals(expectedResult,actualResult)
    }
}