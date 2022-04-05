package com.example.foreverfit.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.foreverfit.converters.NutritionConverter
import com.example.foreverfit.converters.ServingConverter
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "saved_food_table")
data class SavedFood(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var title: String,
    var image: String,
    @TypeConverters(NutritionConverter::class)
    var nutrition: Nutrition,
    @TypeConverters(ServingConverter::class)
    var servings: Serving,
    var useremail: String
    ): Parcelable