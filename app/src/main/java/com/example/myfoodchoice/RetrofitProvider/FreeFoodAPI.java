package com.example.myfoodchoice.RetrofitProvider;


import com.example.myfoodchoice.ModelFreeFoodAPI.Dish;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FreeFoodAPI
{
    @GET("api/json/v1/1/search.php")
    Call<Dish> searchMealByName(@Query("s") String mealName);
}
