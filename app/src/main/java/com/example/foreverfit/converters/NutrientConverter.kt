package com.example.foreverfit.converters

import androidx.room.TypeConverter
import com.example.foreverfit.data.Nutrient
import com.example.foreverfit.data.Serving
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NutrientConverter {

    @TypeConverter
    fun fromNutrientToString(nutrients: List<Nutrient?>?): String? {
        if (nutrients == null)
            return null
        else {
            val gson = Gson()
            val type = object: TypeToken<Nutrient>() {

            }.type
            return gson.toJson(nutrients, type)
            //serijalizacija - iz objekta u json
        }
    }

    @TypeConverter
    fun fromStringToNutrient(string: String): List<Nutrient?>? {
        if (string == null)
            return null
        else {
            val gson = Gson()
            val type = object: TypeToken<Nutrient>() {

            }.type
            return gson.fromJson(string, type)
            //deserijalizacija - iz json-a u objekat
        }
    }
}