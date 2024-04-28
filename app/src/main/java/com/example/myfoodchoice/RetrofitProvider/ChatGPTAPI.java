package com.example.myfoodchoice.RetrofitProvider;

import com.example.myfoodchoice.ModelChatGPT.ChatGPT;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ChatGPTAPI
{
    // todo: must use the firebase instead to store this API_key
    // String API_KEY = ;

    @POST("chat/completions")
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer " + API_KEY
    })
    Call<ChatGPT> getChatCompletion(@Body ChatGPT chatGPT);
}
