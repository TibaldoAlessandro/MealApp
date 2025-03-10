package com.example.mealapp.api

import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("categories.php")
    suspend fun getCategories(): CategoriesResponse

    @GET("list.php?a=list")
    suspend fun getCountries(): CountriesResponse

    @GET("search.php")
    suspend fun searchMealsByName(@Query("s") name: String): MealsResponse

    @GET("lookup.php")
    suspend fun getMealDetails(@Query("i") mealId: String): MealsResponse

    @GET("filter.php")
    suspend fun getMealsByCountry(@Query("a") country: String): MealsResponse
}