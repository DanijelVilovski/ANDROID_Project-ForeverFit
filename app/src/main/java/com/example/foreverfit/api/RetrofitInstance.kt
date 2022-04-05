package com.example.foreverfit.api

import com.example.foreverfit.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //we need to add GsonConverterFactory to convert JSON object to Java object
    }

    val api: FoodApi by lazy {
        retrofit.create(FoodApi::class.java)
    }

}