package com.example.foreverfit.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Serving(
    val size: Double,
    val unit: String
): Parcelable
