package com.example.myfoodchoice.RetrofitProvider;

import com.example.myfoodchoice.ModelChatGPT.ChatRequest;
import com.example.myfoodchoice.ModelChatGPT.FullResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ChatGPTAPI
{
    // todo: must add find the api key from local.properties and place it here.
    String api = "sk-proj-LoiL7m7ISzjAlPQ2Jrn1T3BlbkFJK8wT099DMNOsWDnAwgHz";

    @POST("v1/chat/completions")
    @Headers(
            {
            "Content-Type: application/json",
            "Authorization: Bearer " + api
    })
    Call<FullResponse> getChatCompletion(@Body ChatRequest chatRequest);
}
