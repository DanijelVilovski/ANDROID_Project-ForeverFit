package com.example.foreverfit.converters

import androidx.room.TypeConverter
import com.example.foreverfit.data.Food
import com.example.foreverfit.data.Nutrient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FoodConverter {

    @TypeConverter
    fun fromFoodToString(food: ArrayList<Food?>?): String? {
        if (food == null)
            return null
        else {
            val gson = Gson()
            val type = object: TypeToken<ArrayList<Food?>?>() {

            }.type
            return gson.toJson(food, type)
            //serijalizacija - iz objekta u json
        }
    }

    @TypeConverter
    fun fromStringToFood(string: String): ArrayList<Food?>? {
        if (string == null)
            return null
        else {
            val gson = Gson()
            val type = object: TypeToken<ArrayList<Food?>?>() {

            }.type
            return gson.fromJson(string, type)
            //deserijalizacija - iz json-a u objekat
        }
    }
}