package com.example.foreverfit.converters

import androidx.room.TypeConverter
import com.example.foreverfit.data.Nutrition
import com.google.gson.Gson

class NutritionConverter {

    @TypeConverter
    fun nutritionToString(nutrition: Nutrition): String = Gson().toJson(nutrition)

    @TypeConverter
    fun stringToNutrition(string: String): Nutrition = Gson().fromJson(string, Nutrition::class.java)
}