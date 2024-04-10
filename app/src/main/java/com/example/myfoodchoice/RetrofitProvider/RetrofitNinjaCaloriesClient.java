package com.example.myfoodchoice.RetrofitProvider;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitNinjaCaloriesClient
{
    private static Retrofit retrofit;
    private static final String BASE_URL_NINJA = "https://api.calorieninjas.com/";

    public static Retrofit getRetrofitNinjaInstance()
    {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_NINJA)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
