package com.example.foreverfit.converters

import androidx.room.TypeConverter
import com.example.foreverfit.data.Serving
import com.google.gson.Gson

class ServingConverter {

    @TypeConverter
    fun servingToString(serving: Serving): String = Gson().toJson(serving)

    @TypeConverter
    fun stringToServing(string: String): Serving = Gson().fromJson(string, Serving::class.java)
}