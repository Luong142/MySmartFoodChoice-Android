package com.example.myfoodchoice.RetrofitProvider;

import com.example.myfoodchoice.ModelFreeFoodAPI.RecipeCategories;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FreeFoodRecipeCategoryAPI
{
    @GET("api/json/v1/1/filter.php")
    Call<RecipeCategories> searchRecipeCategory(@Query("c") String category);
}
