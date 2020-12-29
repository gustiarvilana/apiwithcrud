package com.example.mysubmission3.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ModelData (
        var id: Int = 0,
        var userName: String? = null,
        var url: String? = null,
        var image: String? = null
//        var name: String? = null,
//        var blog: String? = null,
//        var company: String? = null,
//        var location: String? = null,
//        var error: String? = null,
//        var followers: Int? = 0,
//        var following: Int? = 0
    ) : Parcelable