package com.example.myfoodchoice.ModelChatGPT;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myfoodchoice.RetrofitProvider.ChatGPTAPI;
import com.example.myfoodchoice.RetrofitProvider.RetrofitChatGPTAPI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallChatAPI
{
    public static void makeChatGPTRequest(String question, String context)
    {
        // todo: it is cheap to use gpt 3.5
        ChatRequest chatRequest = getChatRequest(question, context);

        // Create the API service
        ChatGPTAPI chatGPTAPI = RetrofitChatGPTAPI.getRetrofitChatGPTInstance().create(ChatGPTAPI.class);

        // Make the POST request
        Call<FullResponse> call = chatGPTAPI.getChatCompletion(chatRequest);
        call.enqueue(new Callback<FullResponse>()
        {
            @Override
            public void onResponse(@NonNull Call<FullResponse> call, @NonNull Response<FullResponse> response)
            {
                if (response.isSuccessful())
                {
                    FullResponse chatResponse = response.body();
                    // Handle the response
                    if (chatResponse != null)
                    {
                        for (FullResponse.Choices choices : chatResponse.getChoices())
                        {
                            // get content to get the value

                        }
                    }
                }
                else
                {
                    //addResponse("Error, " + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<FullResponse> call, @NonNull Throwable t)
            {
                //addResponse("Error, " + t.getMessage());
            }
        });
    }

    @NonNull // todo: this method for init the message.
    private static ChatRequest getChatRequest(String question, String context)
    {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setModel("gpt-3.5-turbo");

        ArrayList<ChatRequest.Messages> messages = new ArrayList<>();

        // init message with two types one is for AI, one is for user.
        ChatRequest.Messages messages0 = new ChatRequest.Messages("system", context);
        ChatRequest.Messages messages1 = new ChatRequest.Messages("user", question);

        // add two of them to list.
        messages.add(messages1);
        messages.add(messages0);

        chatRequest.setMessages(messages);
        return chatRequest;
    }
}
