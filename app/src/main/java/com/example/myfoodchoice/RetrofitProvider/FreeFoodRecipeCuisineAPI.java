package com.example.myfoodchoice.RetrofitProvider;

import com.example.myfoodchoice.ModelFreeFoodAPI.RecipeCuisines;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FreeFoodRecipeCuisineAPI
{
    @GET("api/json/v1/1/filter.php")
    Call<RecipeCuisines> searchRecipeCuisine(@Query("a") String cuisine);
}
