package com.example.myfoodchoice.ServiceProviderEdamam;

import com.example.myfoodchoice.ModelEdamam.FoodParseResponse;
import com.example.myfoodchoice.ModelEdamam.RecipeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface EdamamAPI
{
    @GET("api/food-database/v2/parser")
    Call<FoodParseResponse> parseFood(@Query("app_id") String appId,
                                      @Query("app_key") String appKey,
                                      @Query("ingr") String ingredients);
    @GET("search")
    Call<RecipeResponse> searchRecipes(@Query("q") String query,
                                       @Query("app_id") String appId,
                                       @Query("app_key") String appKey);
}
