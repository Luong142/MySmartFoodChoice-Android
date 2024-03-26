package com.example.myfoodchoice.RetrofitProvider;

import com.example.myfoodchoice.ModelCaloriesNinja.FoodItem;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
public interface CaloriesNinjaAPI
{
    @Headers("X-Api-Key: wtsuGH2MhlYMIeB/1AzfVQ==rDZ1zScWPvwReMqa") // replace YOUR_API_KEY_HERE with your api key lol
    @GET("v1/nutrition")
    Call<FoodItem> getFoodItem(@Query("query") String foodName);
}
