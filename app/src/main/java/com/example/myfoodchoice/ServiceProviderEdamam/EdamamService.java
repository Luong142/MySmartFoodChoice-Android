package com.example.myfoodchoice.ServiceProviderEdamam;

import androidx.annotation.NonNull;

import com.example.myfoodchoice.ModelEdamam.RecipeResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EdamamService
{
    String appId = "cc7910a8";
    String appKey = "b7116fce23fe2a6dabbb5b211be93c1b";


    public void searchRecipes(String query)
    {
        EdamamAPI api = RetrofitClient.getRetrofitInstance().create(EdamamAPI.class);
        Call<RecipeResponse> call = api.searchRecipes(query,appId, appKey);

        call.enqueue(new Callback<RecipeResponse>()
        {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response)
            {
                if (response.isSuccessful())
                {
                    RecipeResponse recipeResponse = response.body();
                    // Handle the response
                    System.out.println(recipeResponse);
                }
                else
                {
                    // Handle the error
                    System.out.println("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t)
            {

                // Handle the failure
                System.out.println("Error: " + t.getMessage());
            }
        });
    }
}
