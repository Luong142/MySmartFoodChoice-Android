package com.example.myfoodchoice.ServiceProvider;

import com.example.myfoodchoice.EdamamModel.FoodParseResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface EdamamService
{
    @GET("api/food-database/v2/parser")
    Call<FoodParseResponse> parseFood(@Query("app_id") String appId,
                                      @Query("app_key") String appKey,
                                      @Query("ingr") String ingredients);
}
