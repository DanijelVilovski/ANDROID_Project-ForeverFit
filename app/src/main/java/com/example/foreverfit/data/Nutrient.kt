package com.example.foreverfit.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Nutrient(
    val name: String,
    val amount: Double
): Parcelable