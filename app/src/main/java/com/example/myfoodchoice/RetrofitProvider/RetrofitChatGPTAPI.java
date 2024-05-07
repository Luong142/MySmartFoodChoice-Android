package com.example.myfoodchoice.RetrofitProvider;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitChatGPTAPI
{
    private static Retrofit retrofit;
    private static final String BASE_URL_Free = "https://api.openai.com/";

    public static Retrofit getRetrofitChatGPTInstance()
    {
        if (retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_Free)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
