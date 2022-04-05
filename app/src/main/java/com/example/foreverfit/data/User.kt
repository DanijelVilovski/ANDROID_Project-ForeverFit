package com.example.foreverfit.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.foreverfit.converters.FoodConverter
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var firstName: String,
    var lastName: String,
    var email: String,
    var gender: String,
    var age: String,
    var height: String,
    var weight: String,
    var goal: String?,
    var activityLevel: String,

): Parcelable