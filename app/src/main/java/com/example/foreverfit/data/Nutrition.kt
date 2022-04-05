package com.example.foreverfit.data

import android.os.Parcelable
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.foreverfit.converters.NutrientConverter
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Nutrition(
    @TypeConverters(NutrientConverter::class)
    val nutrients: List<Nutrient>
): Parcelable
