package com.example.foreverfit.api

import androidx.lifecycle.LiveData
import com.example.foreverfit.data.Food
import com.example.foreverfit.data.Result
import com.example.foreverfit.utils.Constants.Companion.API_KEY
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodApi {

    @GET("/food/products/search")
    fun getFoodByName(
        @Query("query") query: String,
        @Query("addProductInformation") info: Boolean,
        @Query("minCalories") minCalories: Int,

        @Query("apiKey") key: String
    ): Call<Result>
}