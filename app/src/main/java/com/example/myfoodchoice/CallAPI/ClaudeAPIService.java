package com.example.myfoodchoice.CallAPI;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
public interface ClaudeAPIService
{
    @Headers({
            "x-api-key: " +
                    "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",
            "anthropic-version: 2023-06-01",
            "Content-Type: application/json"
    })
    @POST("v1/messages")
    Call<MessageResponse> createMessage(@Body MessageRequest messageRequest);
}
