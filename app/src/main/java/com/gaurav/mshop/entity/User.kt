package com.gaurav.mshop.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
        val _id : String? = null,
    var fullName: String? = null,
    var email: String? = null,
    var password: String? = null,
    var mobileNumber: String? = null,
    var address: String? = null,
    var userProfile: String? = null
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var userID : Int = 0

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
        userID = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fullName)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(mobileNumber)
        parcel.writeString(address)
        parcel.writeInt(userID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
