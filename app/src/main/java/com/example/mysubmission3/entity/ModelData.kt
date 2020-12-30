package com.example.mysubmission3.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ModelData (
        var id: String? = null,
        var userName: String? = null,
        var url: String? = null,
        var image: String? = null
    ) : Parcelable