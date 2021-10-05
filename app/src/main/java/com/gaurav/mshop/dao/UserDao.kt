package com.gaurav.mshop.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gaurav.mshop.entity.User

@Dao
interface UserDao {
    // For user register
    @Insert
    suspend fun registerUser(user: User)

    @Query("select * from User where email=(:email) and password=(:password)")
    suspend fun checkUser(email: String, password: String): User

    @Query("SELECT * FROM User" )
    suspend fun getUsers() : List<User>
}