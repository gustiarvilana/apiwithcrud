package com.example.mysubmission3.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ModelData (
        var id: Int = 0,
        var userName: String? = null,
        var url: String? = null,
        var image: String? = null
    ) : Parcelable